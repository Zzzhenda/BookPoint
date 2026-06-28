package com.bookpoint.despachos.controller;

import com.bookpoint.despachos.dto.DespachoRequestDTO;
import com.bookpoint.despachos.dto.DespachoResponseDTO;
import com.bookpoint.despachos.model.EstadoDespacho;
import com.bookpoint.despachos.service.DespachoService;
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
 * API REST de despachos.
 */
@Tag(name = "Despachos", description = "Estado de envio de los pedidos. Consume ms-pedidos.")
@RestController
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
public class DespachoController {

    private final DespachoService despachoService;

    @Operation(summary = "Listar todos los despachos")
    @GetMapping
    public ResponseEntity<List<DespachoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(despachoService.listarTodos());
    }

    @Operation(summary = "Obtener un despacho por id")
    @GetMapping("/{id}")
    public ResponseEntity<DespachoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(despachoService.obtenerPorId(id));
    }

    @Operation(summary = "Obtener el despacho de un pedido")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<DespachoResponseDTO> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(despachoService.obtenerPorPedido(pedidoId));
    }

    @Operation(summary = "Crear el despacho de un pedido")
    @PostMapping
    public ResponseEntity<DespachoResponseDTO> crear(@Valid @RequestBody DespachoRequestDTO dto) {
        DespachoResponseDTO creado = despachoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar el estado de un despacho (PENDIENTE, EN_RUTA, ENTREGADO, CANCELADO)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<DespachoResponseDTO> cambiarEstado(@PathVariable Long id,
                                                             @RequestParam EstadoDespacho estado) {
        return ResponseEntity.ok(despachoService.cambiarEstado(id, estado));
    }

    @Operation(summary = "Eliminar un despacho")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        despachoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
