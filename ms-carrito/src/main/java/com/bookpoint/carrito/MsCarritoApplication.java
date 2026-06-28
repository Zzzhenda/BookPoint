package com.bookpoint.carrito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Carrito de BookPoint Chile.
 * Levanta el servidor en el puerto 8084.
 */
@SpringBootApplication
public class MsCarritoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCarritoApplication.class, args);
    }
}
