package com.multidisciplinar.docsecurepro.bean.ftp;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileLocalRequest {

    private String localFilePath;
    private String remoteFilePath;

}
