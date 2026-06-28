package com.bookpoint.clientes.controller;

import com.bookpoint.clientes.dto.ClienteResponseDTO;
import com.bookpoint.clientes.service.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas del controlador de clientes con @WebMvcTest.
 * Levanta solo la capa web y simula el service con @MockitoBean.
 * El cuerpo JSON se envia como texto directo para no depender de otros beans.
 */
@WebMvcTest(ClienteController.class)
@DisplayName("ClienteController - Pruebas Web")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    @DisplayName("GET /api/clientes/{id} devuelve 200 con el cliente")
    void obtenerPorId_devuelve200() throws Exception {
        ClienteResponseDTO cliente = new ClienteResponseDTO();
        cliente.setId(1L);
        cliente.setNombre("Ana");
        cliente.setApellido("Perez");
        cliente.setEmail("ana@bookpoint.cl");

        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/api/clientes/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@bookpoint.cl"));
    }

    @Test
    @DisplayName("POST /api/clientes devuelve 201 al crear un cliente valido")
    void crear_clienteValido_devuelve201() throws Exception {
        ClienteResponseDTO creado = new ClienteResponseDTO();
        creado.setId(10L);
        creado.setNombre("Ana");
        creado.setApellido("Perez");
        creado.setEmail("ana@bookpoint.cl");

        when(clienteService.crear(any())).thenReturn(creado);

        String body = """
                {
                  "nombre": "Ana",
                  "apellido": "Perez",
                  "email": "ana@bookpoint.cl",
                  "telefono": "+56911112222"
                }
                """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @DisplayName("POST /api/clientes devuelve 400 si el email es invalido")
    void crear_emailInvalido_devuelve400() throws Exception {
        String body = """
                {
                  "nombre": "Ana",
                  "apellido": "Perez",
                  "email": "esto-no-es-un-email"
                }
                """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
