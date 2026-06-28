package com.bookpoint.inventario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI para el microservicio de inventario.
 * Disponible en http://localhost:8083/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventarioOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Inventario")
                .description("Stock por sucursal, ajustes y alertas de reposicion.")
                .version("1.0.0"));
    }
}
