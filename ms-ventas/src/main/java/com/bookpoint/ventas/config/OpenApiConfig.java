package com.bookpoint.ventas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI para el microservicio de ventas.
 * Disponible en http://localhost:8086/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ventasOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Ventas")
                .description("Registra la boleta de un pedido. Consume ms-pedidos por WebClient.")
                .version("1.0.0"));
    }
}
