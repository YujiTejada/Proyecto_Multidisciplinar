package org.example;

import org.example.Dao.usuarios;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FTPDatabaseConnection {
    private DataSource dataSource;
    FTPDatabaseConnection(){
        dataSource = DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost/docsecurepro")
                .username("root")
                .password("").build();
    }
    public usuarios findByNombreUsuario(String nombreUsuario) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        String sql = String.format("SELECT * FROM usuarios WHERE nombre_usuario LIKE '%s';",
                nombreUsuario);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                usuarios userFound = new usuarios(resultSet);
                resultSet.close();
                return userFound;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
