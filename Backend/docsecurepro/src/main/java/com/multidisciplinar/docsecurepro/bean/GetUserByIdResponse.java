package com.multidisciplinar.docsecurepro.bean;

import com.multidisciplinar.docsecurepro.api.dao.User;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByIdResponse {

    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private int idCargo;

    public GetUserByIdResponse(User user) {
        this.idUsuario = user.getIdUsuario();
        this.nombreUsuario = user.getNombreUsuario();
        this.correo = user.getCorreo();
        this.idCargo = user.getIdCargo();
    }

}
