package com.bookpoint.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway de BookPoint Chile.
 *
 * Es el unico punto de entrada del sistema: recibe todas las peticiones en el
 * puerto 8080 y, segun la ruta, las reenvia al microservicio correcto.
 * No tiene logica de negocio: todo el enrutamiento se configura en
 * application.properties.
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
