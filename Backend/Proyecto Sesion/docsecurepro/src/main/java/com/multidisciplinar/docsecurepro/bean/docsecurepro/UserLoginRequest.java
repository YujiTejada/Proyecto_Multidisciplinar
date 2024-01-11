package com.multidisciplinar.docsecurepro.bean.docsecurepro;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserLoginRequest {

    private String nombreUsuario;
    private String contrasenya;

}
