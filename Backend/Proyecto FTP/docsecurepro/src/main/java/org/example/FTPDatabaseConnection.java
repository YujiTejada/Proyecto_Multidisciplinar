package org.example;

import org.example.Dao.usuarios;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;


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

    public usuarios findRolUsuario(String nombreUsuario) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            String sql = "SELECT u.id_usuarios, u.nombre_usuario, u.contrasenya, u.correo, u.id_rol, r.nombre_rol " +
                    "FROM usuarios u INNER JOIN roles r ON u.id_rol = r.id_rol " +
                    "WHERE u.nombre_usuario LIKE ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + nombreUsuario + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new usuarios(
                        resultSet.getInt("id_usuarios"),
                        resultSet.getString("nombre_usuario"),
                        resultSet.getString("contrasenya"),
                        resultSet.getString("correo"),
                        resultSet.getInt("id_rol")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
