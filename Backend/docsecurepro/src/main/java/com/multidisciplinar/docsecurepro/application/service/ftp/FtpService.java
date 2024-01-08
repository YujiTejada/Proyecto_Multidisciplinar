package com.multidisciplinar.docsecurepro.application.service.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class FtpService {

    private FTPClient ftpClient;

    @Autowired
    public FtpService(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public void printTree(String path) {
        try {
            for (FTPFile ftpFile : this.ftpClient.listFiles(path)) {
                System.out.printf("[printTree][%d] Get name : %s \n", System.currentTimeMillis(), ftpFile.getName());
                System.out.printf("[printTree][%d] Get timestamp : %s \n", System.currentTimeMillis(), ftpFile.getTimestamp().getTimeInMillis());
                System.out.printf("[printTree][%d] Get group : %s \n", System.currentTimeMillis(), ftpFile.getGroup());
                System.out.printf("[printTree][%d] Get link : %s \n", System.currentTimeMillis(), ftpFile.getLink());
                System.out.printf("[printTree][%d] Get user : %s \n", System.currentTimeMillis(), ftpFile.getUser());
                System.out.printf("[printTree][%d] Get type : %s \n", System.currentTimeMillis(), ftpFile.getType());
                System.out.printf("[printTree][%d] Is file : %s \n", System.currentTimeMillis(), ftpFile.isFile());
                System.out.printf("[printTree][%d] Is directory : %s \n", System.currentTimeMillis(), ftpFile.isDirectory());
                System.out.printf("[printTree][%d] Formatted string : %s \n", System.currentTimeMillis(), ftpFile.toFormattedString());
                if (ftpFile.isDirectory()) {
                    printTree(path + File.separator + ftpFile.getName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadFile(String fileName) {

    }
}