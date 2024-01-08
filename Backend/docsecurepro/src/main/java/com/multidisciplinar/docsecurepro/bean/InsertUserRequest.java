package com.multidisciplinar.docsecurepro.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InsertUserRequest {

    private String nombreUsuario;
    private String correo;
    private String contrasenya;
    private String idCargo;

}
