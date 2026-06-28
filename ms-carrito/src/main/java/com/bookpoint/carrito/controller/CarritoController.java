package com.bookpoint.carrito.controller;

import com.bookpoint.carrito.dto.CarritoRequestDTO;
import com.bookpoint.carrito.dto.CarritoResponseDTO;
import com.bookpoint.carrito.dto.ItemRequestDTO;
import com.bookpoint.carrito.service.CarritoService;
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
 * API REST del carrito de compra.
 */
@Tag(name = "Carrito", description = "Carrito de compra. Consume ms-productos para validar productos y precios.")
@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @Operation(summary = "Listar todos los carritos")
    @GetMapping
    public ResponseEntity<List<CarritoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(carritoService.listarTodos());
    }

    @Operation(summary = "Ver un carrito con sus items y total")
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear un carrito para un cliente")
    @PostMapping
    public ResponseEntity<CarritoResponseDTO> crear(@Valid @RequestBody CarritoRequestDTO dto) {
        CarritoResponseDTO creado = carritoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Agregar un item al carrito (valida el producto en ms-productos)")
    @PostMapping("/{id}/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(@PathVariable Long id,
                                                          @Valid @RequestBody ItemRequestDTO dto) {
        CarritoResponseDTO actualizado = carritoService.agregarItem(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(actualizado);
    }

    @Operation(summary = "Quitar un item del carrito")
    @DeleteMapping("/{carritoId}/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> quitarItem(@PathVariable Long carritoId,
                                                         @PathVariable Long itemId) {
        return ResponseEntity.ok(carritoService.quitarItem(carritoId, itemId));
    }

    @Operation(summary = "Eliminar un carrito")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
