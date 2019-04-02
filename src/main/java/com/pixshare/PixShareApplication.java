package com.pixshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PixShareApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixShareApplication.class, args);
	}

}
