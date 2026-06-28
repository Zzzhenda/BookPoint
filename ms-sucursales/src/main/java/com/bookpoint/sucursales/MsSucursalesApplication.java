package com.bookpoint.sucursales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Sucursales de BookPoint Chile.
 *
 * Al ejecutar esta clase, Spring Boot levanta el servidor en el puerto 8088
 * y deja disponible la API REST de sucursales.
 */
@SpringBootApplication
public class MsSucursalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSucursalesApplication.class, args);
    }
}
