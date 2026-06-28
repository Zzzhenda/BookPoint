package com.bookpoint.clientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI para el microservicio de clientes.
 * Disponible en http://localhost:8081/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clientesOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Clientes")
                .description("Microservicio de registro, perfil y direcciones de clientes.")
                .version("1.0.0"));
    }
}
