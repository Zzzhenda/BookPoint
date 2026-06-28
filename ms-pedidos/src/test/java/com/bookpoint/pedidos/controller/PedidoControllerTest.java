package com.bookpoint.pedidos.controller;

import com.bookpoint.pedidos.dto.PedidoResponseDTO;
import com.bookpoint.pedidos.model.EstadoPedido;
import com.bookpoint.pedidos.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas del controlador de pedidos con @WebMvcTest.
 */
@WebMvcTest(PedidoController.class)
@DisplayName("PedidoController - Pruebas Web")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @Test
    @DisplayName("GET /api/pedidos/{id} devuelve 200 con el pedido")
    void obtenerPorId_devuelve200() throws Exception {
        PedidoResponseDTO pedido = new PedidoResponseDTO();
        pedido.setId(1L);
        pedido.setClienteId(5L);
        pedido.setEstado(EstadoPedido.CREADO);
        pedido.setTotal(new BigDecimal("2000"));

        when(pedidoService.obtenerPorId(1L)).thenReturn(pedido);

        mockMvc.perform(get("/api/pedidos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CREADO"))
                .andExpect(jsonPath("$.total").value(2000));
    }
}
