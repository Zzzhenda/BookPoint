package com.bookpoint.carrito.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI para el microservicio de carrito.
 * Disponible en http://localhost:8084/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carritoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Carrito")
                .description("Carrito de compra. Consume ms-productos por WebClient.")
                .version("1.0.0"));
    }
}
