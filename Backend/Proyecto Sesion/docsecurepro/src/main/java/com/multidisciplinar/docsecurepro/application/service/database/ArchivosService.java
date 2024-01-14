package com.multidisciplinar.docsecurepro.application.service.database;

import com.multidisciplinar.docsecurepro.api.dao.Archivo;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.InsertUserRequest;
import com.multidisciplinar.docsecurepro.persistence.manager.implementation.ArchivosManagerImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class ArchivosService {

    private DataSource dataSource;
    private ArchivosManagerImplementation manager;

    @Autowired
    public ArchivosService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.manager = new ArchivosManagerImplementation();
    }

    public int insert(Archivo archivo) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return this.manager.insert(connection, archivo);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int deleteByRuta(String ruta) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return this.manager.deleteByRuta(connection, ruta);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Archivo findByRuta(String ruta) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return this.manager.findByRuta(connection, ruta);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
