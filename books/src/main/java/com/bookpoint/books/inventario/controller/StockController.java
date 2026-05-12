package com.bookpoint.books.inventario.controller;

import com.bookpoint.books.inventario.dto.StockDTO;
import com.bookpoint.books.inventario.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StockDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<StockDTO>> buscarPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(service.buscarPorSucursal(sucursalId));
    }

    @PostMapping
    public ResponseEntity<StockDTO> crear(@Valid @RequestBody StockDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody StockDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
