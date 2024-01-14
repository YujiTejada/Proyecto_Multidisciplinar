package com.multidisciplinar.docsecurepro.api.dao;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Archivo {

    private int idArchivo;
    private String nombreArchivo;
    private String ruta;
    private boolean esCarpeta;
    private int idUsuarios;
    private int idCarpeta;

    public Archivo(ResultSet resultSet) {
        try {
            this.idArchivo = resultSet.getInt("id_archivo");
            this.nombreArchivo = resultSet.getString("nombre_archivo");
            this.ruta = resultSet.getString("ruta");
            this.esCarpeta = resultSet.getBoolean("es_carpeta");
            this.idUsuarios = resultSet.getInt("id_usuarios");
            this.idCarpeta = resultSet.getInt("id_carpeta");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
