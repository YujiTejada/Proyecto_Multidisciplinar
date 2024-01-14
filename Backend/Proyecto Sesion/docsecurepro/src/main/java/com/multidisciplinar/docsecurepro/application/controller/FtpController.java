package com.multidisciplinar.docsecurepro.application.controller;

import com.multidisciplinar.docsecurepro.api.dao.Archivo;
import com.multidisciplinar.docsecurepro.api.dao.Log;
import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.application.service.api.FtpService;
import com.multidisciplinar.docsecurepro.bean.ftp.FileInfo;
import com.multidisciplinar.docsecurepro.constants.FtpConstants;
import com.multidisciplinar.docsecurepro.constants.RoleEnum;
import com.multidisciplinar.docsecurepro.constants.TipoOperacionEnum;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.Role;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FtpController {

    private FtpService ftpService;

    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam(name = "currentDirectory",
                    required = false, defaultValue = "/") String currentDirectory,
            HttpSession httpSession) {
        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo debe ser de tipo PDF.");
        }
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConstants.SERVER, FtpConstants.PORT);
            User loggedUser = (User) httpSession.getAttribute("usuario");
            String userRole = determineUserRole(loggedUser.getIdRol());
            ftpClient.login(userRole, "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // Obtener bytes del archivo
            byte[] fileBytes = file.getBytes();
            String uploadRoute = currentDirectory + file.getOriginalFilename();
            // Subir archivo al servidor FTP
            if (ftpClient.storeFile(uploadRoute, new ByteArrayInputStream(fileBytes))) {
                Archivo containerFolder = this.ftpService.findByRuta(currentDirectory);
                Archivo uploadedFileRecord = Archivo.builder()
                        .nombreArchivo(file.getOriginalFilename())
                        .ruta(uploadRoute)
                        .esCarpeta(false)
                        .idUsuarios(loggedUser.getIdUsuario())
                        .idCarpeta(containerFolder.getIdArchivo())
                        .build();
                if (this.ftpService.recordFileUpload(uploadedFileRecord) != 0) {
                    Archivo uploadedFile = this.ftpService.findByRuta(uploadRoute);
                    Log operationRecord = Log.builder()
                            .fechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .idUsuario(loggedUser.getIdUsuario())
                            .tipoOperacion(TipoOperacionEnum.SUBIDA_ARCHIVO.getIdTipoOperacion())
                            .idArchivos(uploadedFile.getIdArchivo()).build();
                    if (this.ftpService.recordMovement(operationRecord) != 0) {
                        return ResponseEntity.ok("File uploaded successfully");
                    } else {
                        return ResponseEntity.ok("File uploaded successfully, " +
                                "but the logging process was not succesful.");
                    }
                } else {
                    return ResponseEntity.ok("File uploaded successfully, " +
                            "but the logging process was not succesful.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping(value = "/create-folder",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createFolder(
            @RequestBody Map<String, String> folderInfo, HttpSession httpSession) {
        String folderName = folderInfo.get("folderName");
        String currentDirectory = folderInfo.get("currentDirectory");
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConstants.SERVER, FtpConstants.PORT);
            User loggedUser = (User) httpSession.getAttribute("usuario");
            String userRole = determineUserRole(loggedUser.getIdRol());
            ftpClient.login(userRole, "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // Crear carpeta en el servidor FTP dentro del directorio actual
            if (ftpClient.makeDirectory(currentDirectory + folderName)) {
                Archivo containerFolder = this.ftpService.findByRuta(currentDirectory);
                Archivo fileRecord = Archivo.builder()
                        .nombreArchivo(folderName)
                        .ruta(currentDirectory + folderName)
                        .esCarpeta(true)
                        .idUsuarios(loggedUser.getIdUsuario())
                        .idCarpeta(containerFolder.getIdArchivo()).build();
                if (this.ftpService.recordFileUpload(fileRecord) != 0) {
                    Archivo uploadedFolder = this.ftpService.findByRuta(currentDirectory + folderName);
                    Log operationRecord = Log.builder()
                            .fechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .idUsuario(loggedUser.getIdUsuario())
                            .tipoOperacion(TipoOperacionEnum.CREACION_CARPETA.getIdTipoOperacion())
                            .idArchivos(uploadedFolder.getIdArchivo()).build();
                    if (this.ftpService.recordMovement(operationRecord) != 0) {
                        Map<String, String> response = new HashMap<>();
                        response.put("status", "success");
                        response.put("message", "Folder created successfully");
                        return ResponseEntity.ok(response);
                    } else {
                        Map<String, String> response = new HashMap<>();
                        response.put("status", "success");
                        response.put("message", "Folder created successfully," +
                                "but the logging process was not succesful.");
                        return ResponseEntity.ok(response);
                    }
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Folder created successfully," +
                            "but the logging process was not succesful.");
                    return ResponseEntity.ok(response);
                }
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Error creating folder");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error creating folder");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @DeleteMapping(value = "/delete-file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteFile(
            @RequestParam(name = "filename") String filename,
            @RequestParam(name = "currentDirectory", required = false, defaultValue = "/") String currentDirectory,
            HttpSession httpSession) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConstants.SERVER, FtpConstants.PORT);
            User loggedUser = (User) httpSession.getAttribute("usuario");
            String userRole = determineUserRole(loggedUser.getIdRol());
            ftpClient.login(userRole, "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String fileRemotePath = currentDirectory + filename;
            // Eliminar archivo en el servidor FTP dentro del directorio actual
            if (ftpClient.deleteFile(fileRemotePath)) {
                Archivo deletedFile = this.ftpService.findByRuta(fileRemotePath);
                Log operationRecord = Log.builder()
                        .fechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .idUsuario(loggedUser.getIdUsuario())
                        .tipoOperacion(TipoOperacionEnum.ELIMINACION_ARCHIVO.getIdTipoOperacion())
                        .idArchivos(deletedFile.getIdArchivo()).build();
                if (this.ftpService.recordMovement(operationRecord) != 0 &&
                        this.ftpService.deleteByRuta(fileRemotePath) != 0) {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "File deleted successfully");
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Folder created successfully," +
                            "but the logging process was not succesful.");
                    return ResponseEntity.ok(response);
                }
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Error deleting file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error deleting file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @DeleteMapping(value = "/delete-folder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteFolder(
            @RequestParam(name = "folderName") String folderName,
            @RequestParam(name = "currentDirectory", required = false, defaultValue = "/") String currentDirectory,
            HttpSession httpSession) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConstants.SERVER, FtpConstants.PORT);
            User loggedUser = (User) httpSession.getAttribute("usuario");
            String userRole = determineUserRole(loggedUser.getIdRol());
            ftpClient.login(userRole, "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // Eliminar carpeta en el servidor FTP dentro del directorio actual
            if (ftpClient.removeDirectory(currentDirectory + folderName)) {
                this.ftpService.deleteByContaingRuta(currentDirectory + folderName);
                Archivo containerFolder = this.ftpService.findByRuta(currentDirectory);
                Log operationRecord = Log.builder()
                        .fechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .idUsuario(loggedUser.getIdUsuario())
                        .tipoOperacion(TipoOperacionEnum.ELIMINACION_CARPETA.getIdTipoOperacion())
                        .idArchivos(containerFolder.getIdArchivo()).build();
                if (this.ftpService.recordMovement(operationRecord) != 0) {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Folder deleted successfully");
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Folder deleted successfully, " +
                            "but there was an error logging the process");
                    return ResponseEntity.ok(response);
                }
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Error deleting folder");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error deleting folder");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @GetMapping(value = "/search-files", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchFiles(
            @RequestParam(name = "searchQuery") String searchQuery,
            @RequestParam(name = "currentDirectory", required = false, defaultValue = "/") String currentDirectory,
            HttpSession httpSession) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConstants.SERVER, FtpConstants.PORT);
            User loggedUser = (User) httpSession.getAttribute("usuario");
            String userRole = determineUserRole(loggedUser.getIdRol());
            ftpClient.login(userRole, "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            List<String> matchingFiles = searchFilesRecursively(ftpClient, currentDirectory, searchQuery);
            System.out.println("Matching files: " + matchingFiles);
            return ResponseEntity.ok(matchingFiles);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during FTP operation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error during FTP operation: " + e.getMessage()));
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                    System.out.println("Disconnected from FTP server");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("filename") String filename,
            @RequestParam("destinationPath") String destinationPath,
            HttpSession httpSession) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConstants.SERVER, FtpConstants.PORT);
            User loggedUser = (User) httpSession.getAttribute("usuario");
            String userRole = determineUserRole(loggedUser.getIdRol());
            ftpClient.login(userRole, "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String remoteFilePath = destinationPath + filename;
            var baos = new ByteArrayOutputStream();
            if (ftpClient.retrieveFile(remoteFilePath, baos)) {
                byte[] fileContent = baos.toByteArray();
                var resource = new ByteArrayResource(fileContent);
                Archivo downloadedFile = this.ftpService.findByRuta(remoteFilePath);
                Log operationRecord = Log.builder()
                        .fechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .idUsuario(loggedUser.getIdUsuario())
                        .tipoOperacion(TipoOperacionEnum.DESCARGA_ARCHIVO.getIdTipoOperacion())
                        .idArchivos(downloadedFile.getIdArchivo()).build();
                this.ftpService.recordMovement(operationRecord);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(fileContent.length)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> listFiles(
            @RequestParam(value = "folderName", defaultValue = "/") String folderName,
            HttpSession httpSession) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConstants.SERVER, FtpConstants.PORT);
            User loggedUser = (User) httpSession.getAttribute("usuario");
            String userRole = determineUserRole(loggedUser.getIdRol());
            ftpClient.login(userRole, "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // Cambia al directorio indicado por el parámetro
            ftpClient.changeWorkingDirectory(folderName);
            // Obtén la lista de nombres de archivos en el directorio actual
            FTPFile[] files = ftpClient.listFiles();
            List<FileInfo> fileList = Arrays.stream(files)
                    .map(file -> {
                        String fileUrl = "ftp://" + userRole + "@"
                                + FtpConstants.SERVER + ":" + FtpConstants.PORT + folderName + file.getName();
                        // Check if the item is a directory based on both isDirectory() and file size
                        boolean isDirectory = file.isDirectory() || file.getSize() == 0;
                        return new FileInfo(file.getName(), isDirectory, fileUrl);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fileList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String determineUserRole(int userRol) {
        String userRole = "";
        if (userRol == RoleEnum.DIRECTIVO.getRoleCode()) {
            userRole = RoleEnum.DIRECTIVO.getRoleName();
        } else if (userRol == RoleEnum.REPRESENTANTE.getRoleCode()) {
            userRole = RoleEnum.REPRESENTANTE.getRoleName();
        } else if (userRol == RoleEnum.SUPER_ADMIN.getRoleCode()) {
            userRole = RoleEnum.SUPER_ADMIN.getRoleName();
        } else {
            userRole = "unknown_role";
        }
        return userRole.toLowerCase();
    }

    private List<String> searchFilesRecursively(
            FTPClient ftpClient, String currentDirectory, String searchQuery) throws IOException {
        FTPFile[] files = ftpClient.listFiles(currentDirectory);
        List<String> matchingFiles = new ArrayList<>();

        for (FTPFile file : files) {
            String fileName = file.getName();
            if (file.isFile() && fileName.contains(searchQuery)) {
                matchingFiles.add(currentDirectory + fileName);
            } else if (file.isDirectory() && !fileName.equals(".") && !fileName.equals("..")) {
                // Recursive call to explore the subdirectory
                List<String> subdirectoryMatchingFiles = searchFilesRecursively(
                        ftpClient, currentDirectory + fileName + "/", searchQuery);
                matchingFiles.addAll(subdirectoryMatchingFiles);
            }
        }

        return matchingFiles;
    }

    private Map<String, String> createErrorResponse(String errorMessage) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", errorMessage);
        return errorResponse;
    }

}
