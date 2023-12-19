package org.example;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/download")
public class FTPFileDownloadController {

    @GetMapping("/{filename}")
    public ResponseEntity<String> downloadFile(@PathVariable("filename") String filename, @RequestParam("destinationPath") String destinationPath) {
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

            // Ruta del archivo en el servidor FTP
            String remoteFilePath = "/" + filename;

            // Ruta donde se guardará el archivo descargado localmente
            String localFilePath = destinationPath + "/" + filename;

            // Descargar archivo desde el servidor FTP
            FileOutputStream fos = new FileOutputStream(localFilePath);
            boolean success = ftpClient.retrieveFile(remoteFilePath, fos);
            fos.close();

            if (success) {
                return ResponseEntity.ok("File downloaded successfully to: " + localFilePath);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading file");
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
}
