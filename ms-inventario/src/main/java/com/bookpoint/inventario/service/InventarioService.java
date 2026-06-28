package com.bookpoint.inventario.service;

import com.bookpoint.inventario.dto.InventarioRequestDTO;
import com.bookpoint.inventario.dto.InventarioResponseDTO;
import com.bookpoint.inventario.exception.RecursoNoEncontradoException;
import com.bookpoint.inventario.model.Inventario;
import com.bookpoint.inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logica de negocio del inventario (stock por sucursal).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    // ------------------------- Lecturas -------------------------

    public List<InventarioResponseDTO> listarTodos() {
        return inventarioRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public InventarioResponseDTO obtenerPorId(Long id) {
        return convertirAResponse(buscarInventarioOFallar(id));
    }

    public List<InventarioResponseDTO> listarPorProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<InventarioResponseDTO> listarPorSucursal(Long sucursalId) {
        return inventarioRepository.findBySucursalId(sucursalId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Productos que estan en o bajo su stock minimo (alertas de reposicion). */
    public List<InventarioResponseDTO> listarAlertasReposicion() {
        return inventarioRepository.findBajoStockMinimo().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // ------------------------- Escrituras -------------------------

    @Transactional
    public InventarioResponseDTO crear(InventarioRequestDTO dto) {
        if (inventarioRepository.existsByProductoIdAndSucursalId(dto.getProductoId(), dto.getSucursalId())) {
            throw new RuntimeException("Ya existe stock para el producto "
                    + dto.getProductoId() + " en la sucursal " + dto.getSucursalId());
        }

        Inventario inventario = new Inventario();
        inventario.setProductoId(dto.getProductoId());
        inventario.setSucursalId(dto.getSucursalId());
        inventario.setCantidad(dto.getCantidad());
        inventario.setStockMinimo(dto.getStockMinimo());

        Inventario guardado = inventarioRepository.save(inventario);
        log.info("Stock creado: producto={} sucursal={} cantidad={}",
                guardado.getProductoId(), guardado.getSucursalId(), guardado.getCantidad());
        return convertirAResponse(guardado);
    }

    @Transactional
    public InventarioResponseDTO actualizar(Long id, InventarioRequestDTO dto) {
        Inventario inventario = buscarInventarioOFallar(id);
        inventario.setProductoId(dto.getProductoId());
        inventario.setSucursalId(dto.getSucursalId());
        inventario.setCantidad(dto.getCantidad());
        inventario.setStockMinimo(dto.getStockMinimo());

        Inventario actualizado = inventarioRepository.save(inventario);
        log.info("Stock actualizado: id={} cantidad={}", actualizado.getId(), actualizado.getCantidad());
        return convertirAResponse(actualizado);
    }

    /**
     * Ajusta el stock sumando (entrada) o restando (salida) unidades.
     * Un delta positivo ingresa mercaderia; uno negativo descuenta una venta/salida.
     * No se permite que el stock quede negativo.
     */
    @Transactional
    public InventarioResponseDTO ajustarStock(Long id, int delta) {
        Inventario inventario = buscarInventarioOFallar(id);
        int nuevaCantidad = inventario.getCantidad() + delta;

        if (nuevaCantidad < 0) {
            throw new RuntimeException("El ajuste deja el stock negativo. "
                    + "Stock actual: " + inventario.getCantidad() + ", ajuste: " + delta);
        }

        inventario.setCantidad(nuevaCantidad);
        Inventario actualizado = inventarioRepository.save(inventario);
        log.info("Stock ajustado: id={} delta={} nuevaCantidad={}", id, delta, nuevaCantidad);
        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Inventario inventario = buscarInventarioOFallar(id);
        inventarioRepository.delete(inventario);
        log.info("Registro de inventario eliminado: id={}", id);
    }

    // ------------------------- Apoyo -------------------------

    private Inventario buscarInventarioOFallar(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el registro de inventario con id " + id));
    }

    private InventarioResponseDTO convertirAResponse(Inventario inventario) {
        InventarioResponseDTO dto = new InventarioResponseDTO();
        dto.setId(inventario.getId());
        dto.setProductoId(inventario.getProductoId());
        dto.setSucursalId(inventario.getSucursalId());
        dto.setCantidad(inventario.getCantidad());
        dto.setStockMinimo(inventario.getStockMinimo());
        // Campo calculado: true si ya alcanzo el minimo y hay que reponer
        dto.setBajoStock(inventario.getCantidad() <= inventario.getStockMinimo());
        return dto;
    }
}
