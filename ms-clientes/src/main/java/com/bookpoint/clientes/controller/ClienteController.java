package com.bookpoint.clientes.controller;

import com.bookpoint.clientes.dto.ClienteRequestDTO;
import com.bookpoint.clientes.dto.ClienteResponseDTO;
import com.bookpoint.clientes.dto.DireccionRequestDTO;
import com.bookpoint.clientes.dto.DireccionResponseDTO;
import com.bookpoint.clientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST de Clientes y sus direcciones.
 * El controller solo orquesta: recibe la peticion, delega en el service y responde.
 */
@Tag(name = "Clientes", description = "Registro, perfil y direcciones de los clientes de BookPoint")
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // ----------------- Clientes -----------------

    @Operation(summary = "Listar todos los clientes")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @Operation(summary = "Obtener un cliente por su id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @Operation(summary = "Crear un cliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO creado = clienteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar un cliente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(clienteService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar un cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------- Direcciones (sub-recurso del cliente) -----------------

    @Operation(summary = "Listar las direcciones de un cliente")
    @GetMapping("/{id}/direcciones")
    public ResponseEntity<List<DireccionResponseDTO>> listarDirecciones(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.listarDirecciones(id));
    }

    @Operation(summary = "Agregar una direccion a un cliente")
    @PostMapping("/{id}/direcciones")
    public ResponseEntity<DireccionResponseDTO> agregarDireccion(@PathVariable Long id,
                                                                @Valid @RequestBody DireccionRequestDTO dto) {
        DireccionResponseDTO creada = clienteService.agregarDireccion(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Eliminar una direccion de un cliente")
    @DeleteMapping("/{clienteId}/direcciones/{direccionId}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long clienteId,
                                                  @PathVariable Long direccionId) {
        clienteService.eliminarDireccion(clienteId, direccionId);
        return ResponseEntity.noContent().build();
    }
}
