package com.bookpoint.books.despachos.controller;

import com.bookpoint.books.despachos.dto.DespachoDTO;
import com.bookpoint.books.despachos.service.DespachoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despachos")
public class DespachoController {

    private final DespachoService service;

    public DespachoController(DespachoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DespachoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespachoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DespachoDTO> crear(@Valid @RequestBody DespachoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PostMapping("/{id}/en-ruta")
    public ResponseEntity<DespachoDTO> marcarEnRuta(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarEnRuta(id));
    }

    @PostMapping("/{id}/entregar")
    public ResponseEntity<DespachoDTO> marcarEntregado(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarEntregado(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<DespachoDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
