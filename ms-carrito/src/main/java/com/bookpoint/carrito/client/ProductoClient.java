package com.bookpoint.carrito.client;

import com.bookpoint.carrito.dto.ProductoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Cliente que consume ms-productos por HTTP usando WebClient.
 *
 * Hace GET /api/productos/{id} y devuelve un Optional:
 *  - Optional con el producto si existe.
 *  - Optional vacio si ms-productos respondio 404 (no existe).
 *  - Lanza una excepcion clara si ms-productos no esta disponible.
 *
 * Usamos .block() para esperar la respuesta de forma sincrona: asi el flujo
 * del carrito es facil de leer y de explicar.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductoClient {

    private final WebClient productosWebClient;

    public Optional<ProductoDTO> buscarProducto(Long productoId) {
        try {
            ProductoDTO producto = productosWebClient.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block();
            return Optional.ofNullable(producto);
        } catch (WebClientResponseException.NotFound e) {
            // ms-productos respondio 404: el producto no existe
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al conectar con ms-productos: {}", e.getMessage());
            throw new RuntimeException("El servicio de productos no esta disponible");
        }
    }
}
