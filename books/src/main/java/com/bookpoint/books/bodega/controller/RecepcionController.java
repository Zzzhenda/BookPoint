package com.bookpoint.books.bodega.controller;

import com.bookpoint.books.bodega.dto.RecepcionDTO;
import com.bookpoint.books.bodega.service.RecepcionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepciones")
public class RecepcionController {

    private final RecepcionService service;

    public RecepcionController(RecepcionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RecepcionDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecepcionDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<RecepcionDTO> crear(@Valid @RequestBody RecepcionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
