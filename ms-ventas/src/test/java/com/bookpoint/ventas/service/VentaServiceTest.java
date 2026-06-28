package com.bookpoint.ventas.service;

import com.bookpoint.ventas.TestDataFactory;
import com.bookpoint.ventas.client.PedidoClient;
import com.bookpoint.ventas.dto.PedidoDTO;
import com.bookpoint.ventas.dto.VentaRequestDTO;
import com.bookpoint.ventas.dto.VentaResponseDTO;
import com.bookpoint.ventas.model.Venta;
import com.bookpoint.ventas.repository.VentaRepository;
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
 * Pruebas unitarias de VentaService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VentaService - Pruebas Unitarias")
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private VentaService ventaService;

    @Test
    @DisplayName("registrar: crea la boleta con el monto del pedido")
    void registrar_pedidoValido_creaVenta() {
        PedidoDTO pedido = TestDataFactory.unPedidoDTO();
        VentaRequestDTO request = TestDataFactory.unRequest(pedido.getId());

        when(ventaRepository.existsByPedidoId(pedido.getId())).thenReturn(false);
        when(pedidoClient.buscarPedido(pedido.getId())).thenReturn(Optional.of(pedido));
        when(ventaRepository.count()).thenReturn(0L);
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        VentaResponseDTO resultado = ventaService.registrar(request);

        assertThat(resultado.getMontoTotal()).isEqualByComparingTo("15000");
        assertThat(resultado.getNumeroBoleta()).isEqualTo("BOL-000001");
        verify(ventaRepository).save(any(Venta.class));
    }

    @Test
    @DisplayName("registrar: lanza excepcion si el pedido ya fue facturado")
    void registrar_pedidoYaFacturado_lanzaExcepcion() {
        VentaRequestDTO request = TestDataFactory.unRequest(1L);
        when(ventaRepository.existsByPedidoId(1L)).thenReturn(true);

        assertThatThrownBy(() -> ventaService.registrar(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya tiene una venta registrada");

        verify(ventaRepository, never()).save(any());
    }

    @Test
    @DisplayName("registrar: lanza excepcion si el pedido no existe")
    void registrar_pedidoNoExiste_lanzaExcepcion() {
        VentaRequestDTO request = TestDataFactory.unRequest(999L);
        when(ventaRepository.existsByPedidoId(999L)).thenReturn(false);
        when(pedidoClient.buscarPedido(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ventaService.registrar(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no existe");

        verify(ventaRepository, never()).save(any());
    }
}
