package org.example;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FTPFileListController {

    private String server = "localhost";
    private int port = 21;
    private String username = "directivo";
    private String password = "";

    @GetMapping
    public ResponseEntity<List<FileInfo>> listFiles(@RequestParam(value = "folderName", defaultValue = "/") String folderName) {
        FTPClient ftpClient = new FTPClient();
        List<FileInfo> fileList = new ArrayList<>();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            // Cambia al directorio indicado por el parámetro
            ftpClient.changeWorkingDirectory(folderName);

            // Obtén la lista de nombres de archivos en el directorio actual
            FTPFile[] files = ftpClient.listFiles();
            fileList = Arrays.stream(files)
                    .map(file -> {
                        String fileUrl = "ftp://" + username + "@" + server + ":" + port + folderName + file.getName();
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
}
