package com.multidisciplinar.docsecurepro.bean.ftp;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    private String name;
    private boolean isDirectory;
    private String url;

}
