package com.bookpoint.pedidos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Define los dos WebClient que usa pedidos: uno para ms-clientes y otro para
 * ms-carrito. Cada bean tiene su propia URL base sacada de application.properties.
 */
@Configuration
public class WebClientConfig {

    @Value("${clientes.url}")
    private String clientesUrl;

    @Value("${carrito.url}")
    private String carritoUrl;

    @Bean
    public WebClient clientesWebClient() {
        return WebClient.builder().baseUrl(clientesUrl).build();
    }

    @Bean
    public WebClient carritoWebClient() {
        return WebClient.builder().baseUrl(carritoUrl).build();
    }
}
