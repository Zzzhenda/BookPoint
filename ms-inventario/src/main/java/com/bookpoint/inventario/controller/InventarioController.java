package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.dto.InventarioRequestDTO;
import com.bookpoint.inventario.dto.InventarioResponseDTO;
import com.bookpoint.inventario.service.InventarioService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST del inventario (stock por sucursal).
 */
@Tag(name = "Inventario", description = "Stock de productos por sucursal y alertas de reposicion")
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @Operation(summary = "Listar todo el inventario")
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @Operation(summary = "Obtener un registro de inventario por id")
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @Operation(summary = "Stock de un producto en todas las sucursales")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioResponseDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.listarPorProducto(productoId));
    }

    @Operation(summary = "Stock de todos los productos de una sucursal")
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<InventarioResponseDTO>> listarPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(inventarioService.listarPorSucursal(sucursalId));
    }

    @Operation(summary = "Alertas: productos en o bajo su stock minimo")
    @GetMapping("/alertas")
    public ResponseEntity<List<InventarioResponseDTO>> listarAlertas() {
        return ResponseEntity.ok(inventarioService.listarAlertasReposicion());
    }

    @Operation(summary = "Registrar stock de un producto en una sucursal")
    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crear(@Valid @RequestBody InventarioRequestDTO dto) {
        InventarioResponseDTO creado = inventarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar un registro de inventario")
    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.ok(inventarioService.actualizar(id, dto));
    }

    @Operation(summary = "Ajustar stock (suma con delta positivo, descuenta con negativo)")
    @PutMapping("/{id}/ajuste")
    public ResponseEntity<InventarioResponseDTO> ajustarStock(@PathVariable Long id,
                                                              @RequestParam int cantidad) {
        return ResponseEntity.ok(inventarioService.ajustarStock(id, cantidad));
    }

    @Operation(summary = "Eliminar un registro de inventario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
