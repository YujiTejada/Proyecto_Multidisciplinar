package com.multidisciplinar.docsecurepro.persistence.manager.implementation;

import com.multidisciplinar.docsecurepro.api.dao.Log;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor
public class LogManagerImplementation {

    public int insert(Connection connection, Log log) {
        var sql = String.format("INSERT INTO log(fecha_hora, id_usuario, tipo_operacion, id_archivos) " +
                        "VALUES('%s', %d, %d, %d);",
                log.getFechaHora(), log.getIdUsuario(), log.getTipoOperacion(), log.getIdArchivos());
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
