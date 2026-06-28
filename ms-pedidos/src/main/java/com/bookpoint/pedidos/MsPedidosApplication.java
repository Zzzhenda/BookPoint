package com.bookpoint.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Pedidos de BookPoint Chile.
 * Levanta el servidor en el puerto 8085.
 */
@SpringBootApplication
public class MsPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPedidosApplication.class, args);
    }
}
