package com.multidisciplinar.docsecurepro.bean.docsecurepro;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMailRequest {

    private String recipient;
    private String subject;
    private String message;

}
