package com.bookpoint.pedidos.service;

import com.bookpoint.pedidos.TestDataFactory;
import com.bookpoint.pedidos.client.CarritoClient;
import com.bookpoint.pedidos.client.ClienteClient;
import com.bookpoint.pedidos.dto.CarritoDTO;
import com.bookpoint.pedidos.dto.ClienteDTO;
import com.bookpoint.pedidos.dto.PedidoRequestDTO;
import com.bookpoint.pedidos.dto.PedidoResponseDTO;
import com.bookpoint.pedidos.model.EstadoPedido;
import com.bookpoint.pedidos.model.Pedido;
import com.bookpoint.pedidos.repository.PedidoRepository;
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
 * Pruebas unitarias de PedidoService.
 *
 * Mockeamos el repositorio y los dos clientes remotos (clientes y carrito),
 * para probar el flujo de generacion del pedido sin levantar otros servicios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService - Pruebas Unitarias")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private CarritoClient carritoClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("crear: genera el pedido con sus detalles y total cuando todo es valido")
    void crear_clienteYCarritoValidos_generaPedido() {
        ClienteDTO cliente = TestDataFactory.unClienteDTO();
        CarritoDTO carrito = TestDataFactory.unCarritoConUnItem(cliente.getId());
        PedidoRequestDTO request = TestDataFactory.unRequest(cliente.getId(), carrito.getId());

        when(clienteClient.buscarCliente(cliente.getId())).thenReturn(Optional.of(cliente));
        when(carritoClient.obtenerCarrito(carrito.getId())).thenReturn(Optional.of(carrito));
        // Devolvemos el mismo pedido que se intenta guardar
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        PedidoResponseDTO resultado = pedidoService.crear(request);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPedido.CREADO);
        assertThat(resultado.getDetalles()).hasSize(1);
        assertThat(resultado.getTotal()).isEqualByComparingTo("2000");
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion si el cliente no existe")
    void crear_clienteNoExiste_lanzaExcepcion() {
        PedidoRequestDTO request = TestDataFactory.unRequest(999L, 1L);
        when(clienteClient.buscarCliente(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no existe");

        // Si el cliente no existe, ni siquiera se consulta el carrito
        verify(carritoClient, never()).obtenerCarrito(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear: lanza excepcion si el carrito esta vacio")
    void crear_carritoVacio_lanzaExcepcion() {
        ClienteDTO cliente = TestDataFactory.unClienteDTO();
        CarritoDTO carrito = TestDataFactory.unCarritoConUnItem(cliente.getId());
        carrito.setItems(java.util.List.of()); // carrito vacio
        PedidoRequestDTO request = TestDataFactory.unRequest(cliente.getId(), carrito.getId());

        when(clienteClient.buscarCliente(cliente.getId())).thenReturn(Optional.of(cliente));
        when(carritoClient.obtenerCarrito(carrito.getId())).thenReturn(Optional.of(carrito));

        assertThatThrownBy(() -> pedidoService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("carrito vacio");

        verify(pedidoRepository, never()).save(any());
    }
}
