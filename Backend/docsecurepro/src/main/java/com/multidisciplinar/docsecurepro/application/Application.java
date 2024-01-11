package com.multidisciplinar.docsecurepro.application;

import com.multidisciplinar.docsecurepro.constants.JdbcConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DataSource dataSource() {
		DataSource dataSource = DataSourceBuilder.create()
				.driverClassName(JdbcConstants.DRIVER_CLASS_NAME)
				.url(JdbcConstants.URL)
				.username(JdbcConstants.USERNAME)
				.password(JdbcConstants.PASSWORD).build();
		return dataSource;
	}

}
