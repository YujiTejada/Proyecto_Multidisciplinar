package com.multidisciplinar.docsecurepro.application.service.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FtpService {

    private FTPClient ftpClient;
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String DOWNLOADS = "C:\\Users\\" + USER_DIR + "\\Downloads";

    @Autowired
    public FtpService(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String createDirectory(String directoryPath) {
        try {
            if (!checkIfDirectoryExists(directoryPath)) {
                if (ftpClient.makeDirectory(directoryPath)) {
                    return "2";
                } else {
                    return "1";
                }
            } else {
                // Bloque de código si existe el archivo. Preguntar si se desea sustituir.
                return "0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String deleteDirectory(String directoryPath) {
        try {
            if (checkIfDirectoryExists(directoryPath)) {
                if (ftpClient.removeDirectory(directoryPath)) {
                    return "2";
                } else {
                    return "1";
                }
            } else {
                return "0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String uploadFileLocal(String localFilePath, String remoteFilePath) {
        try {
            if (!checkIfFileExists(remoteFilePath)) {
                var fileInputStream = new FileInputStream(localFilePath);
                if (ftpClient.storeFile(remoteFilePath, fileInputStream)) {
                    return "2";
                } else {
                    return "1";
                }
            } else {
                // Bloque de código si existe el archivo. Preguntar si se desea sustituir.
                return "0";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String downloadFileLocal(String remoteFilePath) {
        try {
            if (checkIfFileExists(remoteFilePath)) {
                File file = new File(DOWNLOADS + remoteFilePath.substring(
                        remoteFilePath.lastIndexOf("\""), remoteFilePath.length()));
                // ¿Comprobar si ya existe ese archivo descargado en la carpeta?
                if (file.createNewFile()) {
                    var fos = new FileOutputStream(file);
                    if (ftpClient.retrieveFile(remoteFilePath, fos)) {
                        return "3";
                    } else {
                        return "2";
                    }
                } else {
                    return "1";
                }
            } else {
                return "0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String deleteFile(String remoteFilePath) {
        try {
            if (checkIfFileExists(remoteFilePath)) {
                if (ftpClient.deleteFile(remoteFilePath)) {
                    return "2";
                } else {
                    return "1";
                }
            } else {
                return "0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ByteArrayResource downloadFileRemote(String fileName, String destinationPath) {
        ftpClient.enterLocalPassiveMode();
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            var serverFilePath = new StringBuilder()
                    .append("/")
                    .append(fileName);
            var baos = new ByteArrayOutputStream();
            boolean success = ftpClient.retrieveFile(serverFilePath.toString(), baos);
            if (success) {
                byte[] fileContent = baos.toByteArray();
                var resource = new ByteArrayResource(fileContent);
                return resource;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String uploadFileRemote(MultipartFile file, String remoteFilePath) {
        try {
            if (!file.getContentType().equals("application/pdf")) {
                return "0";
            }
            byte[] fileBytes = file.getBytes();
            if (ftpClient.storeFile(remoteFilePath, new ByteArrayInputStream(fileBytes))) {
                return "2";
            } else {
                return "1";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkIfDirectoryExists(String directoryPath) {
        try {
            ftpClient.changeWorkingDirectory(directoryPath);
            int returnCode = ftpClient.getReplyCode();
            if (returnCode == 550) {
                return false;
            }
            return true;
        } catch (IOException e) {
            // Puede que haga falta devolver String (hay 3 situaciones de return).
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkIfFileExists(String filePath) {
        try {
            var inputStream = ftpClient.retrieveFileStream(filePath);
            int returnCode = ftpClient.getReplyCode();
            if (inputStream == null || returnCode == 550) {
                return false;
            }
            return true;
        } catch (IOException e) {
            // Puede que haga falta devolver String (hay 3 situaciones de return).
            e.printStackTrace();
            return false;
        }
    }

}