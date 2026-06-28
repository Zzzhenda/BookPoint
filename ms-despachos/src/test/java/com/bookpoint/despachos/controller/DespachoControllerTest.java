package com.bookpoint.despachos.controller;

import com.bookpoint.despachos.dto.DespachoResponseDTO;
import com.bookpoint.despachos.model.EstadoDespacho;
import com.bookpoint.despachos.service.DespachoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas del controlador de despachos con @WebMvcTest.
 */
@WebMvcTest(DespachoController.class)
@DisplayName("DespachoController - Pruebas Web")
class DespachoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DespachoService despachoService;

    @Test
    @DisplayName("GET /api/despachos/{id} devuelve 200 con el despacho")
    void obtenerPorId_devuelve200() throws Exception {
        DespachoResponseDTO despacho = new DespachoResponseDTO();
        despacho.setId(1L);
        despacho.setPedidoId(7L);
        despacho.setEstado(EstadoDespacho.PENDIENTE);
        despacho.setDireccionEnvio("Barros Arana 1234, Concepcion");

        when(despachoService.obtenerPorId(1L)).thenReturn(despacho);

        mockMvc.perform(get("/api/despachos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.pedidoId").value(7));
    }
}
