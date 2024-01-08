package com.multidisciplinar.docsecurepro.application;

import com.multidisciplinar.docsecurepro.constants.FtpConstants;
import com.multidisciplinar.docsecurepro.constants.JdbcConstants;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.io.IOException;

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

	@Bean
	public FTPClient ftpClient() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(FtpConstants.HOST, FtpConstants.PORT);
			ftpClient.login(FtpConstants.USERNAME, FtpConstants.PASSWORD);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return ftpClient;
	}

}
