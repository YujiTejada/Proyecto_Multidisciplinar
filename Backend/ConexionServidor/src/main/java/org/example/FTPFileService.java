package org.example;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FTPFileService {

    private String server = "localhost";
    private int port = 21;
    private String username = "representante";
    private String password = "";
    private String baseDirectory = "/";

    public List<FileInfo> getFilesInFolder(String folderName) {
        FTPClient ftpClient = new FTPClient();
        List<FileInfo> fileList = new ArrayList<>();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            // Cambiar el directorio al especificado
            ftpClient.changeWorkingDirectory(baseDirectory + folderName);

            // Obtén la lista de nombres de archivos en el directorio actual
            FTPFile[] files = ftpClient.listFiles();
            fileList = Arrays.stream(files)
                    .map(file -> {
                        String fileUrl = "ftp://" + username + "@" + server + ":" + port + baseDirectory + folderName + "/" + file.getName();
                        return new FileInfo(file.getName(), file.isDirectory(), fileUrl);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
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

        return fileList;
    }
}
