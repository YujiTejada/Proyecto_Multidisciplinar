package com.multidisciplinar.docsecurepro.persistence.manager.implementation;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.InsertUserRequest;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserManagerImplementation {

    public int insert(Connection connection, InsertUserRequest user) {
        var sql = String.format("INSERT INTO usuario(nombreUsuario, correo, contrasenya, idCargo) " +
                        "VALUES('%s', '%s', '%s', %d);",
                user.getNombreUsuario(), user.getCorreo(), user.getContrasenya(), Integer.valueOf(user.getIdCargo()));
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll(Connection connection) {
        var sql = "SELECT * FROM usuario;";
        try (Statement statement = connection.createStatement()) {
            var usersRetrieved = new ArrayList<User>();
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                usersRetrieved.add(new User(resultSet));
            }
            resultSet.close();
            return usersRetrieved;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByNombreUsuario(Connection connection, String nombreUsuario) {
        var sql = String.format("SELECT * FROM usuario WHERE nombreUsuario LIKE '%s';",
                nombreUsuario);
        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                var userFound = new User(resultSet);
                resultSet.close();
                return userFound;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByIdUsuario(Connection connection, int id) {
        var sql = String.format("SELECT * FROM usuario WHERE idUsuario = %d;",
                id);
        try (Statement statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                var userFound = new User(resultSet);
                resultSet.close();
                return userFound;
            }
            resultSet.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
