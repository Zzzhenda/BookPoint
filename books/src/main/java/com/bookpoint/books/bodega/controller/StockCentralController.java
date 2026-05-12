package com.bookpoint.books.bodega.controller;

import com.bookpoint.books.bodega.dto.StockCentralDTO;
import com.bookpoint.books.bodega.service.StockCentralService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-central")
public class StockCentralController {

    private final StockCentralService service;

    public StockCentralController(StockCentralService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StockCentralDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockCentralDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<StockCentralDTO> crear(@Valid @RequestBody StockCentralDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockCentralDTO> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody StockCentralDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
