package com.bookpoint.despachos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI para el microservicio de despachos.
 * Disponible en http://localhost:8087/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI despachosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Despachos")
                .description("Estado de envio de los pedidos. Consume ms-pedidos por WebClient.")
                .version("1.0.0"));
    }
}
