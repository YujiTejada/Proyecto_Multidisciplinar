package com.multidisciplinar.docsecurepro.bean.docsecurepro;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    private String nombreUsuario;
    private String contrasenya;

}
