package com.multidisciplinar.docsecurepro.api.dao;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    private int idLog;
    private String fechaHora;
    private int idUsuario;
    private int tipoOperacion;
    private int idArchivos;

    public Log(ResultSet resultSet) {
        try {
            this.idLog = resultSet.getInt("id_log");
            this.fechaHora = resultSet.getDate("fecha_hora").toString();
            this.idUsuario = resultSet.getInt("id_usuario");
            this.tipoOperacion = resultSet.getInt("tipo_operacion");
            this.idArchivos = resultSet.getInt("id_archivos");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
