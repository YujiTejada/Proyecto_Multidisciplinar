package com.multidisciplinar.docsecurepro.api.dao;

import lombok.*;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private String contrasenya;
    private int idRol;

    public User(ResultSet resultSet){
        try {
            this.idUsuario = resultSet.getInt("id_usuarios");
            this.nombreUsuario = resultSet.getString("nombre_usuario");
            this.correo = resultSet.getString("correo");
            this.contrasenya = resultSet.getString("contrasenya");
            this.idRol = resultSet.getInt("id_rol");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
