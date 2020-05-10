package com.microservice.storageapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class StorageApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageApiApplication.class, args);
	}

}
