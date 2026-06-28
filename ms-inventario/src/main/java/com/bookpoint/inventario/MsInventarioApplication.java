package com.bookpoint.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Inventario de BookPoint Chile.
 * Levanta el servidor en el puerto 8083.
 */
@SpringBootApplication
public class MsInventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsInventarioApplication.class, args);
    }
}
