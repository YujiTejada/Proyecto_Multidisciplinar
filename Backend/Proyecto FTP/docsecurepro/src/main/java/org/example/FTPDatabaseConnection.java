package org.example;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;


public class FTPDatabaseConnection {
    private DataSource dataSource;
    FTPDatabaseConnection(){
        dataSource = DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost/docsecurepro")
                .username("root")
                .password("").build();
    }

    public String insertarArchivoEnBaseDeDatos(){
        String sql = "INSERT INTO log ()";
        return "";
    }
}
