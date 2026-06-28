package com.bookpoint.ventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Ventas de BookPoint Chile.
 * Levanta el servidor en el puerto 8086.
 */
@SpringBootApplication
public class MsVentasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsVentasApplication.class, args);
    }
}
