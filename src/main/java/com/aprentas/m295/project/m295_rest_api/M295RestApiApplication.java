package com.aprentas.m295.project.m295_rest_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse der Spring Boot-Anwendung.
 * Diese Klasse initialisiert die Spring Boot-Anwendung und startet den Webserver.
 */
@SpringBootApplication
public class M295RestApiApplication {

	/**
     * Hauptmethode zum Starten der Anwendung.
     * 
     * @param args - Kommandozeilenargumente
     */
	public static void main(String[] args) {
		SpringApplication.run(M295RestApiApplication.class, args);
	}

}
