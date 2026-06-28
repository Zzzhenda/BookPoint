package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.dto.InventarioResponseDTO;
import com.bookpoint.inventario.service.InventarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas del controlador de inventario con @WebMvcTest.
 */
@WebMvcTest(InventarioController.class)
@DisplayName("InventarioController - Pruebas Web")
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService inventarioService;

    @Test
    @DisplayName("GET /api/inventario/{id} devuelve 200 con el stock")
    void obtenerPorId_devuelve200() throws Exception {
        InventarioResponseDTO inventario = new InventarioResponseDTO();
        inventario.setId(1L);
        inventario.setProductoId(10L);
        inventario.setSucursalId(2L);
        inventario.setCantidad(20);
        inventario.setStockMinimo(5);
        inventario.setBajoStock(false);

        when(inventarioService.obtenerPorId(1L)).thenReturn(inventario);

        mockMvc.perform(get("/api/inventario/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(20))
                .andExpect(jsonPath("$.bajoStock").value(false));
    }

    @Test
    @DisplayName("GET /api/inventario/alertas devuelve 200 con los productos bajo minimo")
    void listarAlertas_devuelve200() throws Exception {
        InventarioResponseDTO bajo = new InventarioResponseDTO();
        bajo.setId(3L);
        bajo.setProductoId(7L);
        bajo.setSucursalId(1L);
        bajo.setCantidad(2);
        bajo.setStockMinimo(5);
        bajo.setBajoStock(true);

        when(inventarioService.listarAlertasReposicion()).thenReturn(List.of(bajo));

        mockMvc.perform(get("/api/inventario/alertas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bajoStock").value(true));
    }
}
