package com.bookpoint.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Productos (catalogo) de BookPoint Chile.
 * Levanta el servidor en el puerto 8082.
 */
@SpringBootApplication
public class MsProductosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsProductosApplication.class, args);
    }
}
