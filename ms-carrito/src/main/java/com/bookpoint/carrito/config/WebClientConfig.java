package com.bookpoint.carrito.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuracion del WebClient que usa el carrito para hablar con ms-productos.
 *
 * La URL base sale de application.properties (productos.url), asi no queda
 * "quemada" en el codigo y se puede cambiar sin recompilar.
 */
@Configuration
public class WebClientConfig {

    @Value("${productos.url}")
    private String productosUrl;

    @Bean
    public WebClient productosWebClient() {
        return WebClient.builder()
                .baseUrl(productosUrl)
                .build();
    }
}
