package com.bookpoint.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicacion principal de BookPoint Chile.
 * Levanta el contexto de Spring Boot y deja la API REST escuchando
 * en el puerto definido en application.properties (por defecto 8080).
 */
@SpringBootApplication
public class BooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class, args);
    }
}
