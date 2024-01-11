package com.multidisciplinar.docsecurepro.bean.docsecurepro;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SendMailRequest {

    private String recipient;
    private String subject;
    private String message;

}
