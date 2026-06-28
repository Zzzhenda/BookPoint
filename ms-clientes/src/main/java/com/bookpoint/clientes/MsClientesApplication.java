package com.bookpoint.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque del microservicio de Clientes de BookPoint Chile.
 *
 * Levanta el servidor en el puerto 8081 y expone la API REST de clientes
 * y sus direcciones.
 */
@SpringBootApplication
public class MsClientesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsClientesApplication.class, args);
    }
}
