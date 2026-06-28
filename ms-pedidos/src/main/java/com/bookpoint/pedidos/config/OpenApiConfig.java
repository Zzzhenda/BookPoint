package com.bookpoint.pedidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI para el microservicio de pedidos.
 * Disponible en http://localhost:8085/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pedidosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Pedidos")
                .description("Genera pedidos desde un carrito. Consume ms-clientes y ms-carrito.")
                .version("1.0.0"));
    }
}
