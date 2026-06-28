package com.bookpoint.despachos.service;

import com.bookpoint.despachos.TestDataFactory;
import com.bookpoint.despachos.client.PedidoClient;
import com.bookpoint.despachos.dto.DespachoRequestDTO;
import com.bookpoint.despachos.dto.DespachoResponseDTO;
import com.bookpoint.despachos.dto.PedidoDTO;
import com.bookpoint.despachos.model.Despacho;
import com.bookpoint.despachos.model.EstadoDespacho;
import com.bookpoint.despachos.repository.DespachoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de DespachoService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DespachoService - Pruebas Unitarias")
class DespachoServiceTest {

    @Mock
    private DespachoRepository despachoRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private DespachoService despachoService;

    @Test
    @DisplayName("crear: crea el despacho en estado PENDIENTE cuando el pedido existe")
    void crear_pedidoValido_creaDespacho() {
        PedidoDTO pedido = TestDataFactory.unPedidoDTO();
        DespachoRequestDTO request = TestDataFactory.unRequest(pedido.getId());

        when(despachoRepository.existsByPedidoId(pedido.getId())).thenReturn(false);
        when(pedidoClient.buscarPedido(pedido.getId())).thenReturn(Optional.of(pedido));
        when(despachoRepository.save(any(Despacho.class))).thenAnswer(inv -> inv.getArgument(0));

        DespachoResponseDTO resultado = despachoService.crear(request);

        assertThat(resultado.getEstado()).isEqualTo(EstadoDespacho.PENDIENTE);
        verify(despachoRepository).save(any(Despacho.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion si el pedido ya tiene despacho")
    void crear_pedidoConDespacho_lanzaExcepcion() {
        DespachoRequestDTO request = TestDataFactory.unRequest(1L);
        when(despachoRepository.existsByPedidoId(1L)).thenReturn(true);

        assertThatThrownBy(() -> despachoService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya tiene un despacho");

        verify(despachoRepository, never()).save(any());
    }

    @Test
    @DisplayName("cambiarEstado: al marcar ENTREGADO registra la fecha de entrega")
    void cambiarEstado_entregado_registraFechaEntrega() {
        Despacho despacho = TestDataFactory.unDespacho();
        when(despachoRepository.findById(despacho.getId())).thenReturn(Optional.of(despacho));
        when(despachoRepository.save(any(Despacho.class))).thenAnswer(inv -> inv.getArgument(0));

        DespachoResponseDTO resultado = despachoService.cambiarEstado(despacho.getId(), EstadoDespacho.ENTREGADO);

        assertThat(resultado.getEstado()).isEqualTo(EstadoDespacho.ENTREGADO);
        assertThat(resultado.getFechaEntrega()).isNotNull();
    }
}
