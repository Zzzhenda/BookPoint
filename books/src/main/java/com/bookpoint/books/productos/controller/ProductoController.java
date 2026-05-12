package com.bookpoint.books.productos.controller;

import com.bookpoint.books.productos.dto.ProductoDTO;
import com.bookpoint.books.productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST de productos. Solo orquesta peticiones, no contiene
 * logica (esa vive en el service).
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<ProductoDTO>> buscarPorAutor(@PathVariable String autor) {
        return ResponseEntity.ok(service.buscarPorAutor(autor));
    }

    @GetMapping("/editorial/{editorial}")
    public ResponseEntity<List<ProductoDTO>> buscarPorEditorial(@PathVariable String editorial) {
        return ResponseEntity.ok(service.buscarPorEditorial(editorial));
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<ProductoDTO>> buscarPorGenero(@PathVariable String genero) {
        return ResponseEntity.ok(service.buscarPorGenero(genero));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id,
                                                  @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
