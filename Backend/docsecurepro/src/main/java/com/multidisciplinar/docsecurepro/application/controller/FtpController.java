package com.multidisciplinar.docsecurepro.application.controller;

import com.multidisciplinar.docsecurepro.application.service.ftp.FtpService;
import com.multidisciplinar.docsecurepro.bean.ftp.UploadFileLocalRequest;
import com.multidisciplinar.docsecurepro.constants.ApiConstants;
import com.multidisciplinar.docsecurepro.constants.FtpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@CrossOrigin(origins = "http://localhost:4200",
        maxAge = 1800,
        allowCredentials = "true",
        allowedHeaders = {"*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.HEAD,
                RequestMethod.TRACE, RequestMethod.OPTIONS, RequestMethod.PATCH},
        exposedHeaders = {"set-cookie"})
@RequestMapping(ApiConstants.FTP_ENDPOINT)
public class FtpController {

    private FtpService ftpService;

    @Autowired
    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @GetMapping("/download/fileRemote/{fileName}")
    public ResponseEntity<? extends Resource> downloadFileRemote(
            @PathVariable String fileName, @RequestParam String destinationPath) {
        var resource = this.ftpService.downloadFileRemote(fileName, destinationPath);
        if (resource != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.ok()
                    .body(null);
        }
    }

    @PostMapping(value = "/upload/fileRemote/{filePath}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFileRemote(
            @RequestPart (name="file", required = true) MultipartFile file,
            @PathVariable String filePath) {
        String result = this.ftpService.uploadFileRemote(file, filePath);
        if (result != null) {
            if (result.equals("0")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_is_not_pdf");
            } else if (result.equals("1")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("error_uploading_file");
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_uploaded_succesfully");
            }
        } else {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("server_exception_thrown");
        }
    }

    @PostMapping("/upload/directory/{directoryPath}")
    public ResponseEntity<String> createDirectory(@PathVariable String directoryPath) {
        String result = this.ftpService.createDirectory(directoryPath);
        if (result != null) {
            if (result.equals("0")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("directory_already_exists");
            } else if (result.equals("1")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("error_creating_directory");
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("directory_created_succesfully");
            }
        } else {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("server_exception_thrown");
        }
    }

    @DeleteMapping("/delete/directory/{directoryPath}")
    public ResponseEntity<String> deleteDirectory(@PathVariable String directoryPath) {
        String result = this.ftpService.deleteDirectory(directoryPath);
        if (result != null) {
            if (result.equals("0")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("folder_does_not_exist");
            } else if (result.equals("1")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("error_deleting_folder");
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("folder_deleted_succesfully");
            }
        } else {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("server_exception_thrown");
        }
    }

    @PostMapping("/upload/fileLocal/{filePath}")
    public ResponseEntity<String> uploadFileLocal(@RequestBody UploadFileLocalRequest uploadFileLocalRequest) {
        String result = this.ftpService.uploadFileLocal(uploadFileLocalRequest.getLocalFilePath(),
                uploadFileLocalRequest.getRemoteFilePath());
        if (result != null) {
            if (result.equals("0")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_already_exists");
            } else if (result.equals("1")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("error_uploading_file");
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_uploaded_succesfully");
            }
        } else {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("server_exception_thrown");
        }
    }

    @GetMapping("download/fileLocal")
    public ResponseEntity<String> downloadFileLocal(@RequestParam String remoteFilePath) {
        String result = this.ftpService.downloadFileLocal(remoteFilePath);
        if (result != null) {
            if (result.equals("0")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_does_not_exist");
            } else if (result.equals("1")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("error_creating_local_file");
            } else if (result.equals("2")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("error_downloading_file");
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_downloaded_succesfully");
            }
        } else {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("server_exception_thrown");
        }
    }

    @DeleteMapping("/delete/file/{filePath}")
    public ResponseEntity<String> deleteFile(@PathVariable String remoteFilePath) {
        String result = this.ftpService.deleteFile(remoteFilePath);
        if (result != null) {
            if (result.equals("0")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_does_not_exist");
            } else if (result.equals("1")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("error_deleting_file");
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("file_deleted_succesfully");
            }
        } else {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("server_exception_thrown");
        }
    }

}
