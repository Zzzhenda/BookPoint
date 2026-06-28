package com.bookpoint.despachos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Despachos de BookPoint Chile.
 * Levanta el servidor en el puerto 8087.
 */
@SpringBootApplication
public class MsDespachosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsDespachosApplication.class, args);
    }
}
