package org.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FTPFileDeleteController {
    private String server = "localhost";
    private int port = 21;
    private String username = "representante";
    private String password = "";
    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            boolean success = ftpClient.login(username, password);

            if (!success) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de autenticación FTP");
            }

            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conexión fallida");
            }
            // Configurar el cliente FTP para que elimine el archivo
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Eliminar el archivo
            success = ftpClient.deleteFile(filename);

            if (success) {
                return ResponseEntity.ok("Archivo eliminado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el archivo");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de E/S: " + e.getMessage());
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
            }
        }
    }
}
