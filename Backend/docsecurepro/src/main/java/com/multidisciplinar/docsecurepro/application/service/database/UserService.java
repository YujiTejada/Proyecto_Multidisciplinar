package com.multidisciplinar.docsecurepro.application.service.database;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.bean.InsertUserRequest;
import com.multidisciplinar.docsecurepro.persistence.manager.implementation.UserManagerImplementation;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Getter
@Service
public class UserService {

    private DataSource dataSource;
    private UserManagerImplementation manager;

    @Autowired
    public UserService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.manager = new UserManagerImplementation();
    }

    public int insert(InsertUserRequest user) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return manager.insert(connection, user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public List<User> selectAll() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return manager.findAll(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public User findByNombreUsuario(String nombreUsuario) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return manager.findByNombreUsuario(connection, nombreUsuario);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public User findById(int id) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return manager.findById(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
