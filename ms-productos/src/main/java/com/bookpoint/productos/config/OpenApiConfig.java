package com.bookpoint.productos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI para el microservicio de productos.
 * Disponible en http://localhost:8082/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Productos")
                .description("Catalogo de libros y utiles. Permite filtrar por autor, genero y precio.")
                .version("1.0.0"));
    }
}
