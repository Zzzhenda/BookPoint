package com.bookpoint.pedidos.client;

import com.bookpoint.pedidos.dto.ClienteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Cliente que consume ms-clientes para validar que el cliente existe.
 * GET /api/clientes/{id} -> Optional<ClienteDTO>.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteClient {

    private final WebClient clientesWebClient;

    public Optional<ClienteDTO> buscarCliente(Long clienteId) {
        try {
            ClienteDTO cliente = clientesWebClient.get()
                    .uri("/api/clientes/{id}", clienteId)
                    .retrieve()
                    .bodyToMono(ClienteDTO.class)
                    .block();
            return Optional.ofNullable(cliente);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al conectar con ms-clientes: {}", e.getMessage());
            throw new RuntimeException("El servicio de clientes no esta disponible");
        }
    }
}
