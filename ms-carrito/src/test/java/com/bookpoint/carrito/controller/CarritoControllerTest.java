package com.bookpoint.carrito.controller;

import com.bookpoint.carrito.dto.CarritoResponseDTO;
import com.bookpoint.carrito.service.CarritoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas del controlador de carrito con @WebMvcTest.
 */
@WebMvcTest(CarritoController.class)
@DisplayName("CarritoController - Pruebas Web")
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoService carritoService;

    @Test
    @DisplayName("GET /api/carritos/{id} devuelve 200 con el carrito y su total")
    void obtenerPorId_devuelve200() throws Exception {
        CarritoResponseDTO carrito = new CarritoResponseDTO();
        carrito.setId(1L);
        carrito.setClienteId(5L);
        carrito.setItems(List.of());
        carrito.setTotal(new BigDecimal("3000"));

        when(carritoService.obtenerPorId(1L)).thenReturn(carrito);

        mockMvc.perform(get("/api/carritos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(5))
                .andExpect(jsonPath("$.total").value(3000));
    }
}
