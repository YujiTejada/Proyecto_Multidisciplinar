package com.multidisciplinar.docsecurepro.bean.docsecurepro;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserRequest {

    private String nombreUsuario;
    private String correo;
    private String contrasenya;
    private String cargo;

}
