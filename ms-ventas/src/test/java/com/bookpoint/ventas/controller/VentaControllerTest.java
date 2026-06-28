package com.bookpoint.ventas.controller;

import com.bookpoint.ventas.dto.VentaResponseDTO;
import com.bookpoint.ventas.model.MetodoPago;
import com.bookpoint.ventas.service.VentaService;
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
 * Pruebas del controlador de ventas con @WebMvcTest.
 */
@WebMvcTest(VentaController.class)
@DisplayName("VentaController - Pruebas Web")
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService ventaService;

    @Test
    @DisplayName("GET /api/ventas/{id} devuelve 200 con la boleta")
    void obtenerPorId_devuelve200() throws Exception {
        VentaResponseDTO venta = new VentaResponseDTO();
        venta.setId(1L);
        venta.setPedidoId(7L);
        venta.setNumeroBoleta("BOL-000001");
        venta.setMontoTotal(new BigDecimal("15000"));
        venta.setMetodoPago(MetodoPago.DEBITO);

        when(ventaService.obtenerPorId(1L)).thenReturn(venta);

        mockMvc.perform(get("/api/ventas/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroBoleta").value("BOL-000001"))
                .andExpect(jsonPath("$.metodoPago").value("DEBITO"));
    }
}
