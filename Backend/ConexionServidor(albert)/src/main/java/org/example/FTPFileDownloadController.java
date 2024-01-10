package org.example;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/download")
public class FTPFileDownloadController {

    @GetMapping("/{filename}")
    public ResponseEntity<? extends Resource> downloadFile(@PathVariable("filename") String filename, @RequestParam("destinationPath") String destinationPath) {
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

            // Descargar archivo desde el servidor FTP
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean success = ftpClient.retrieveFile(remoteFilePath, baos);

            if (success) {
                byte[] fileContent = baos.toByteArray();
                ByteArrayResource resource = new ByteArrayResource(fileContent);

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
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

