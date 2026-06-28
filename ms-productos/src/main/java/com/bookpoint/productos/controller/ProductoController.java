package com.bookpoint.productos.controller;

import com.bookpoint.productos.dto.ProductoRequestDTO;
import com.bookpoint.productos.dto.ProductoResponseDTO;
import com.bookpoint.productos.service.ProductoService;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * API REST del catalogo de productos (libros y utiles).
 */
@Tag(name = "Productos", description = "Catalogo de libros y utiles de BookPoint")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(summary = "Obtener un producto por su id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @Operation(summary = "Filtrar productos por autor")
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorAutor(@PathVariable String autor) {
        return ResponseEntity.ok(productoService.buscarPorAutor(autor));
    }

    @Operation(summary = "Filtrar productos por genero")
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorGenero(@PathVariable String genero) {
        return ResponseEntity.ok(productoService.buscarPorGenero(genero));
    }

    @Operation(summary = "Filtrar productos por precio maximo")
    @GetMapping("/precio-maximo/{precio}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorPrecioMaximo(@PathVariable BigDecimal precio) {
        return ResponseEntity.ok(productoService.buscarPorPrecioMaximo(precio));
    }

    @Operation(summary = "Crear un producto")
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO creado = productoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar un producto")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
