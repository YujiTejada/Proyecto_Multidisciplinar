package org.example;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FTPFileListController {

    private String server = "localhost";
    private int port = 21;
    private String username = "directivo";
    private String password = "";
    @GetMapping
    public ResponseEntity<List<String>> listFiles() {
        FTPClient ftpClient = new FTPClient();
        List<String> fileList = new ArrayList<>();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // Obtén la lista de nombres de archivos en el directorio actual
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                fileList.add(file.getName());
            }

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
}
