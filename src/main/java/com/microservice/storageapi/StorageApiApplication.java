package com.microservice.storageapi;

import com.microservice.storageapi.services.MediaManagerStorageService;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;


@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class StorageApiApplication {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource mainDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();}

	public static void main(String[] args) {
		SpringApplication.run(StorageApiApplication.class, args);
	}


}
