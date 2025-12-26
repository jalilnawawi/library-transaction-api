package com.spring_api.library_transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LibraryTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryTransactionApplication.class, args);
	}

}
