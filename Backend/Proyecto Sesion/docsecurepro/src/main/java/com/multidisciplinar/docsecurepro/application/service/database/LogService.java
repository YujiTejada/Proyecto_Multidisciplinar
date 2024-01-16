package com.multidisciplinar.docsecurepro.application.service.database;

import com.multidisciplinar.docsecurepro.api.dao.Archivo;
import com.multidisciplinar.docsecurepro.api.dao.Log;
import com.multidisciplinar.docsecurepro.persistence.manager.implementation.ArchivosManagerImplementation;
import com.multidisciplinar.docsecurepro.persistence.manager.implementation.LogManagerImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class LogService {

    private DataSource dataSource;
    private LogManagerImplementation manager;

    @Autowired
    public LogService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.manager = new LogManagerImplementation();
    }

    public int insert(Log log) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return this.manager.insert(connection, log);
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
}
