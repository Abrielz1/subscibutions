package com.subsributions.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Subscriptions Service API",
				version = "1.0",
				description = "API для управления подписками и пользователями",
				contact = @Contact(
						name = "Abriel",
						email = "почта"
				),
				license = @License(
						name = "GPL v3.0",
						url = "https://subs.com/license"
				)
		),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Локальный сервер разработки"
		)
)

@SpringBootApplication
public class SubscibutionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscibutionsApplication.class, args);
	}
}
