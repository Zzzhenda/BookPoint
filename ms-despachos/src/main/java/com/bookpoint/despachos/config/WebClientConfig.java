package com.bookpoint.despachos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient para consultar ms-pedidos. La URL base sale de application.properties.
 */
@Configuration
public class WebClientConfig {

    @Value("${pedidos.url}")
    private String pedidosUrl;

    @Bean
    public WebClient pedidosWebClient() {
        return WebClient.builder().baseUrl(pedidosUrl).build();
    }
}
