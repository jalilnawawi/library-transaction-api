package com.spring_api.library_transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@EnableJpaAuditing
@SpringBootApplication
public class LibraryTransactionApplication {

	public static void main(String[] args) {
        loadDotEnv(".env");
		SpringApplication.run(LibraryTransactionApplication.class, args);
	}

    private static final Logger log = LoggerFactory.getLogger(LibraryTransactionApplication.class);

    private static void loadDotEnv(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines
                    .filter(line -> !line.startsWith("#") && line.contains("="))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            System.setProperty(key, value);
                        }
                    });
        } catch (IOException e) {
            log.error("Could not load .env file: {}", e.getMessage());
        }
    }
}
