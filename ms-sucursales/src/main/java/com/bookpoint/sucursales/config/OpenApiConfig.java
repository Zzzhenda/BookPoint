package com.bookpoint.sucursales.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI.
 *
 * Personaliza el titulo y la descripcion que se ven en la pagina
 * http://localhost:8088/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sucursalesOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BookPoint Chile - API de Sucursales")
                .description("Microservicio que administra las sucursales (Concepcion, Temuco, La Serena).")
                .version("1.0.0"));
    }
}
