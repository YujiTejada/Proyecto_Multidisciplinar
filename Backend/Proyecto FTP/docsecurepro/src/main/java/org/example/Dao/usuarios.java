package org.example.Dao;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class usuarios {

    private int idUsuarios;
    private String nombreUsuario;
    private String contrasenya;
    private String correo;
    private int idRol;

    public usuarios(ResultSet resultSet){

        try {
            this.idUsuarios = resultSet.getInt("id_usuarios");
            this.nombreUsuario = resultSet.getString("nombre_usuario");
            this.correo = resultSet.getString("correo");
            this.contrasenya = resultSet.getString("contrasenya");
            this.idRol = resultSet.getInt("id_rol");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
