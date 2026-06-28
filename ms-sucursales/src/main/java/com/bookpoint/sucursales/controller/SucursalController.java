package com.bookpoint.sucursales.controller;

import com.bookpoint.sucursales.dto.SucursalRequestDTO;
import com.bookpoint.sucursales.dto.SucursalResponseDTO;
import com.bookpoint.sucursales.service.SucursalService;
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
 * API REST de Sucursales.
 *
 * El controller solo se encarga de recibir la peticion HTTP, delegar en el
 * service y devolver la respuesta con su codigo de estado. No tiene logica
 * de negocio (eso vive en SucursalService).
 */
@Tag(name = "Sucursales", description = "Gestion de las sucursales de BookPoint Chile")
@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @Operation(summary = "Listar todas las sucursales")
    @GetMapping
    public ResponseEntity<List<SucursalResponseDTO>> listarTodas() {
        return ResponseEntity.ok(sucursalService.listarTodas());
    }

    @Operation(summary = "Obtener una sucursal por su id")
    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }

    @Operation(summary = "Listar las sucursales de una ciudad")
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<SucursalResponseDTO>> listarPorCiudad(@PathVariable String ciudad) {
        return ResponseEntity.ok(sucursalService.listarPorCiudad(ciudad));
    }

    @Operation(summary = "Crear una nueva sucursal")
    @PostMapping
    public ResponseEntity<SucursalResponseDTO> crear(@Valid @RequestBody SucursalRequestDTO dto) {
        SucursalResponseDTO creada = sucursalService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Actualizar una sucursal existente")
    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody SucursalRequestDTO dto) {
        return ResponseEntity.ok(sucursalService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar una sucursal")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
