package com.bookpoint.books.pedidos.client;

import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import com.bookpoint.books.pedidos.dto.PedidoDTO;
import com.bookpoint.books.pedidos.model.EstadoPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Cliente HTTP que consume el microservicio de pedidos via WebClient.
 * Lo usa el microservicio de despachos para no acoplarse a la implementacion
 * interna de pedidos (cumple IE 2.4.1 de la rubrica).
 *
 * Maneja timeouts y errores remotos traduciendolos a las excepciones
 * propias del dominio para que el GlobalExceptionHandler las atrape.
 */
@Component
public class PedidoClient {

    private static final Logger log = LoggerFactory.getLogger(PedidoClient.class);

    private final WebClient webClient;

    public PedidoClient(WebClient bookpointWebClient) {
        this.webClient = bookpointWebClient;
    }

    /** Verifica que un pedido exista. Retorna el DTO o lanza 404. */
    public PedidoDTO obtener(Long pedidoId) {
        log.info("[WebClient] GET /api/pedidos/{}", pedidoId);
        try {
            return webClient.get()
                    .uri("/api/pedidos/{id}", pedidoId)
                    .retrieve()
                    .bodyToMono(PedidoDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RecursoNoEncontradoException("Pedido " + pedidoId + " no existe");
            }
            log.error("[WebClient] Error consultando pedido {}: {}", pedidoId, e.getMessage());
            throw new ReglaNegocioException("Error al consultar el pedido " + pedidoId);
        } catch (Exception e) {
            log.error("[WebClient] Timeout/red al consultar pedido {}: {}", pedidoId, e.getMessage());
            throw new ReglaNegocioException("Servicio de pedidos no disponible");
        }
    }

    /** Cambia el estado de un pedido remoto. Lo usa el despacho al entregar. */
    public void cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        log.info("[WebClient] PUT /api/pedidos/{}/estado?estado={}", pedidoId, nuevoEstado);
        try {
            webClient.put()
                    .uri(b -> b.path("/api/pedidos/{id}/estado")
                            .queryParam("estado", nuevoEstado).build(pedidoId))
                    .retrieve()
                    .bodyToMono(PedidoDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("[WebClient] Error cambiando estado del pedido {}: {}", pedidoId, e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo actualizar el pedido " + pedidoId + ": " + e.getStatusCode());
        } catch (Exception e) {
            log.error("[WebClient] Timeout/red al actualizar pedido {}: {}", pedidoId, e.getMessage());
            throw new ReglaNegocioException("Servicio de pedidos no disponible");
        }
    }
}
