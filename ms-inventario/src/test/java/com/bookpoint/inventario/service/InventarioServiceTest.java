package com.bookpoint.inventario.service;

import com.bookpoint.inventario.TestDataFactory;
import com.bookpoint.inventario.dto.InventarioRequestDTO;
import com.bookpoint.inventario.dto.InventarioResponseDTO;
import com.bookpoint.inventario.model.Inventario;
import com.bookpoint.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de InventarioService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioService - Pruebas Unitarias")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    @DisplayName("crear: registra el stock cuando no existe ese producto en esa sucursal")
    void crear_combinacionNueva_guardaStock() {
        InventarioRequestDTO request = TestDataFactory.unRequestValido();
        Inventario guardado = TestDataFactory.unInventario();

        when(inventarioRepository.existsByProductoIdAndSucursalId(
                request.getProductoId(), request.getSucursalId())).thenReturn(false);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(guardado);

        InventarioResponseDTO resultado = inventarioService.crear(request);

        assertThat(resultado).isNotNull();
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion si ya existe stock de ese producto en esa sucursal")
    void crear_combinacionDuplicada_lanzaExcepcion() {
        InventarioRequestDTO request = TestDataFactory.unRequestValido();
        when(inventarioRepository.existsByProductoIdAndSucursalId(
                request.getProductoId(), request.getSucursalId())).thenReturn(true);

        assertThatThrownBy(() -> inventarioService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya existe stock");

        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("ajustarStock: descuenta unidades correctamente")
    void ajustarStock_descuentoValido_actualizaCantidad() {
        Inventario inventario = TestDataFactory.unInventario();
        inventario.setCantidad(20);
        when(inventarioRepository.findById(inventario.getId())).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        InventarioResponseDTO resultado = inventarioService.ajustarStock(inventario.getId(), -5);

        assertThat(resultado.getCantidad()).isEqualTo(15);
    }

    @Test
    @DisplayName("ajustarStock: lanza excepcion si el ajuste deja el stock negativo")
    void ajustarStock_dejaNegativo_lanzaExcepcion() {
        Inventario inventario = TestDataFactory.unInventario();
        inventario.setCantidad(3);
        when(inventarioRepository.findById(inventario.getId())).thenReturn(Optional.of(inventario));

        assertThatThrownBy(() -> inventarioService.ajustarStock(inventario.getId(), -10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("stock negativo");

        verify(inventarioRepository, never()).save(any());
    }
}
