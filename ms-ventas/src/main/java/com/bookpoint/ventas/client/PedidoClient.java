package com.bookpoint.ventas.client;

import com.bookpoint.ventas.dto.PedidoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Cliente que consume ms-pedidos para validar el pedido y obtener su total.
 * GET /api/pedidos/{id} 
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoClient {

    private final WebClient pedidosWebClient;

    public Optional<PedidoDTO> buscarPedido(Long pedidoId) {
        try {
            PedidoDTO pedido = pedidosWebClient.get()
                    .uri("/api/pedidos/{id}", pedidoId)
                    .retrieve()
                    .bodyToMono(PedidoDTO.class)
                    .block();
            return Optional.ofNullable(pedido);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al conectar con ms-pedidos: {}", e.getMessage());
            throw new RuntimeException("El servicio de pedidos no esta disponible");
        }
    }
}
