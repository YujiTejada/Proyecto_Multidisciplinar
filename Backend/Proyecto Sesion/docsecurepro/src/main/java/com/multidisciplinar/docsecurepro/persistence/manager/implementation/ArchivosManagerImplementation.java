package com.multidisciplinar.docsecurepro.persistence.manager.implementation;

import com.multidisciplinar.docsecurepro.api.dao.Archivo;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.InsertUserRequest;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.arch.Processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor
public class ArchivosManagerImplementation {

    public int insert(Connection connection, Archivo archivo) {
        var sql = String.format("INSERT INTO archivos(nombre_archivo, ruta, carpeta_no, id_usuarios, id_carpeta) " +
                        "VALUES('%s', '%s', null, %d, %d);",
                archivo.getNombreArchivo(), archivo.getRuta(), archivo.getIdUsuarios(), archivo.getIdCarpeta());
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Archivo findByRuta(Connection connection, String ruta) {
        var sql = String.format("SELECT * FROM archivos WHERE ruta LIKE '%s';", ruta);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Archivo archivo = new Archivo(resultSet);
                resultSet.close();
                return archivo;
            }
            resultSet.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deleteByRuta(Connection connection, String ruta) {
        var sql = String.format("DELETE FROM archivos WHERE ruta LIKE '%s';", ruta);
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
