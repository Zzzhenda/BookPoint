package com.bookpoint.pedidos.client;

import com.bookpoint.pedidos.dto.CarritoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Cliente que consume ms-carrito para traer el carrito con sus items.
 * GET /api/carritos/{id} -> Optional<CarritoDTO>.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CarritoClient {

    private final WebClient carritoWebClient;

    public Optional<CarritoDTO> obtenerCarrito(Long carritoId) {
        try {
            CarritoDTO carrito = carritoWebClient.get()
                    .uri("/api/carritos/{id}", carritoId)
                    .retrieve()
                    .bodyToMono(CarritoDTO.class)
                    .block();
            return Optional.ofNullable(carrito);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al conectar con ms-carrito: {}", e.getMessage());
            throw new RuntimeException("El servicio de carrito no esta disponible");
        }
    }
}
