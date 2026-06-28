package com.bookpoint.sucursales.controller;

import com.bookpoint.sucursales.dto.SucursalResponseDTO;
import com.bookpoint.sucursales.service.SucursalService;
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
 * Pruebas del controlador con @WebMvcTest.
 *
 * Levanta solo la capa web (sin base de datos) y simula el service con
 * @MockitoBean para verificar que los endpoints responden correctamente.
 */
@WebMvcTest(SucursalController.class)
@DisplayName("SucursalController - Pruebas Web")
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SucursalService sucursalService;

    @Test
    @DisplayName("GET /api/sucursales devuelve 200 con la lista de sucursales")
    void listarTodas_devuelve200() throws Exception {
        SucursalResponseDTO sucursal = new SucursalResponseDTO();
        sucursal.setId(1L);
        sucursal.setNombre("BookPoint Concepcion Centro");
        sucursal.setCiudad("Concepcion");

        when(sucursalService.listarTodas()).thenReturn(List.of(sucursal));

        mockMvc.perform(get("/api/sucursales").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("BookPoint Concepcion Centro"))
                .andExpect(jsonPath("$[0].ciudad").value("Concepcion"));
    }

    @Test
    @DisplayName("GET /api/sucursales/{id} devuelve 200 con la sucursal pedida")
    void obtenerPorId_devuelve200() throws Exception {
        SucursalResponseDTO sucursal = new SucursalResponseDTO();
        sucursal.setId(5L);
        sucursal.setNombre("BookPoint Temuco");
        sucursal.setCiudad("Temuco");

        when(sucursalService.obtenerPorId(5L)).thenReturn(sucursal);

        mockMvc.perform(get("/api/sucursales/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.ciudad").value("Temuco"));
    }
}
