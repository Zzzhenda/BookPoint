package com.bookpoint.productos.controller;

import com.bookpoint.productos.dto.ProductoResponseDTO;
import com.bookpoint.productos.model.TipoProducto;
import com.bookpoint.productos.service.ProductoService;
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
 * Pruebas del controlador de productos con @WebMvcTest.
 */
@WebMvcTest(ProductoController.class)
@DisplayName("ProductoController - Pruebas Web")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Test
    @DisplayName("GET /api/productos/{id} devuelve 200 con el producto")
    void obtenerPorId_devuelve200() throws Exception {
        ProductoResponseDTO producto = new ProductoResponseDTO();
        producto.setId(1L);
        producto.setTitulo("Cien Anos de Soledad");
        producto.setTipo(TipoProducto.LIBRO);
        producto.setPrecio(new BigDecimal("12990"));

        when(productoService.obtenerPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Cien Anos de Soledad"))
                .andExpect(jsonPath("$.tipo").value("LIBRO"));
    }

    @Test
    @DisplayName("GET /api/productos/genero/{genero} devuelve 200 con la lista filtrada")
    void buscarPorGenero_devuelve200() throws Exception {
        ProductoResponseDTO producto = new ProductoResponseDTO();
        producto.setId(2L);
        producto.setTitulo("Dune");
        producto.setGenero("Ciencia Ficcion");
        producto.setTipo(TipoProducto.LIBRO);

        when(productoService.buscarPorGenero("Ciencia Ficcion")).thenReturn(java.util.List.of(producto));

        mockMvc.perform(get("/api/productos/genero/Ciencia Ficcion").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Dune"));
    }
}
