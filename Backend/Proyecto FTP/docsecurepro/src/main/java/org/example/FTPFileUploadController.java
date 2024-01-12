package org.example;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FTPFileUploadController {
    private String nombre_usuario;
    private FTPDatabaseConnection ftpDataBase = new FTPDatabaseConnection();
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart(name = "file", required = true) MultipartFile file,
            @RequestParam(name = "currentDirectory", required = false, defaultValue = "/") String currentDirectory) {
        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo debe ser de tipo PDF.");
        }
        String server = "localhost";
        int port = 21;
        String username = "directivo";
        String password = "";
        String destinationFolder = "/";
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Obtener bytes del archivo
            byte[] fileBytes = file.getBytes();

            // Subir archivo al servidor FTP
            boolean success = ftpClient.storeFile(currentDirectory + "/" + file.getOriginalFilename(), new ByteArrayInputStream(fileBytes));
            if (success) {
                return ResponseEntity.ok("File uploaded successfully");
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

    @PostMapping(value = "/create-folder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createFolder(@RequestBody Map<String, String> folderInfo) {
        String folderName = folderInfo.get("folderName");
        String currentDirectory = folderInfo.get("currentDirectory");
        String server = "localhost";
        int port = 21;
        String username = "directivo";
        String password = "";
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Crear carpeta en el servidor FTP dentro del directorio actual
            boolean success = ftpClient.makeDirectory(currentDirectory + folderName);

            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Folder created successfully");
                return ResponseEntity.ok(response);
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
            @RequestParam(name = "currentDirectory", required = false, defaultValue = "/") String currentDirectory) {

        String server = "localhost";
        int port = 21;
        String username = "directivo";
        String password = "";
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Eliminar archivo en el servidor FTP dentro del directorio actual
            boolean success = ftpClient.deleteFile(currentDirectory + "/" + filename);

            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "File deleted successfully");
                return ResponseEntity.ok(response);
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
            @RequestParam(name = "currentDirectory", required = false, defaultValue = "/") String currentDirectory) {

        String server = "localhost";
        int port = 21;
        String username = "directivo";
        String password = "";
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Eliminar carpeta en el servidor FTP dentro del directorio actual
            boolean success = ftpClient.removeDirectory(currentDirectory + folderName);

            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Folder deleted successfully");
                return ResponseEntity.ok(response);
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

    @PostMapping(value="/upload-user/{nombreUsuario}")
    public void setNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario) {
        this.nombre_usuario = nombreUsuario;
    }
}
