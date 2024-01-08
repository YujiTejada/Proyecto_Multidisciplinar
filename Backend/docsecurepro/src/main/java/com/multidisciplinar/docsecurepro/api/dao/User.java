package com.multidisciplinar.docsecurepro.api.dao;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private String contrasenya;
    private int idCargo;

    public User(ResultSet resultSet) {

        try {
            this.idUsuario = resultSet.getInt("idUsuario");
            this.nombreUsuario = resultSet.getString("nombreUsuario");
            this.correo = resultSet.getString("correo");
            this.contrasenya = resultSet.getString("contrasenya");
            this.idCargo = resultSet.getInt("idCargo");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
