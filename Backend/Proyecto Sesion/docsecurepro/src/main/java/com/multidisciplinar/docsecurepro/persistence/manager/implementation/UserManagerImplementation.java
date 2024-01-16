package com.multidisciplinar.docsecurepro.persistence.manager.implementation;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.InsertUserRequest;
import com.multidisciplinar.docsecurepro.constants.RoleEnum;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserManagerImplementation {

    public int insert(Connection connection, InsertUserRequest user) {
        int cargo = 0;
        if (user.getCargo().equals(RoleEnum.DIRECTIVO.getRoleName())) {
            cargo = RoleEnum.DIRECTIVO.getRoleCode();
        } else if (user.getCargo().equals(RoleEnum.REPRESENTANTE.getRoleName())) {
            cargo = RoleEnum.REPRESENTANTE.getRoleCode();
        } else if (user.getCargo().equals(RoleEnum.SUPER_ADMIN.getRoleName())) {
            cargo = RoleEnum.SUPER_ADMIN.getRoleCode();
        }
        var sql = String.format("INSERT INTO usuarios(dni, nombre_usuario, contrasenya, correo, id_rol) " +
                        "VALUES(null, '%s', '%s', '%s', %d);",
                user.getNombreUsuario(), user.getContrasenya(), user.getCorreo(), cargo);
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<User> findAll(Connection connection) {
        var sql = "SELECT * FROM usuarios;";
        try (Statement statement = connection.createStatement()) {
            var usersRetrieved = new ArrayList<User>();
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                usersRetrieved.add(new User(resultSet));
            }
            resultSet.close();
            return usersRetrieved;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findByNombreUsuario(Connection connection, String nombreUsuario) {
        var sql = String.format("SELECT * FROM usuarios WHERE nombre_usuario LIKE '%s';",
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
            e.printStackTrace();
            return null;
        }
    }

    public User findByIdUsuario(Connection connection, int id) {
        var sql = String.format("SELECT * FROM usuarios WHERE id_usuarios = %d;",
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
            e.printStackTrace();
            return null;
        }
    }

}
