package com.bookpoint.ventas.controller;

import com.bookpoint.ventas.dto.VentaRequestDTO;
import com.bookpoint.ventas.dto.VentaResponseDTO;
import com.bookpoint.ventas.service.VentaService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST de ventas (boletas).
 */
@Tag(name = "Ventas", description = "Registra la boleta de un pedido. Consume ms-pedidos.")
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @Operation(summary = "Listar todas las ventas")
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @Operation(summary = "Obtener una venta por id")
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    @Operation(summary = "Obtener la venta de un pedido")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<VentaResponseDTO> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(ventaService.obtenerPorPedido(pedidoId));
    }

    @Operation(summary = "Registrar la venta de un pedido")
    @PostMapping
    public ResponseEntity<VentaResponseDTO> registrar(@Valid @RequestBody VentaRequestDTO dto) {
        VentaResponseDTO creada = ventaService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Eliminar una venta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
