package com.bookpoint.productos.service;

import com.bookpoint.productos.TestDataFactory;
import com.bookpoint.productos.dto.ProductoRequestDTO;
import com.bookpoint.productos.dto.ProductoResponseDTO;
import com.bookpoint.productos.exception.RecursoNoEncontradoException;
import com.bookpoint.productos.model.Producto;
import com.bookpoint.productos.repository.ProductoRepository;
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
 * Pruebas unitarias de ProductoService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoService - Pruebas Unitarias")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("crear: guarda el producto cuando los datos son validos")
    void crear_datosValidos_guardaProducto() {
        ProductoRequestDTO request = TestDataFactory.unRequestLibroValido();
        Producto guardado = TestDataFactory.unLibro();

        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        ProductoResponseDTO resultado = productoService.crear(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(guardado.getId());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion si un LIBRO no trae autor")
    void crear_libroSinAutor_lanzaExcepcion() {
        ProductoRequestDTO request = TestDataFactory.unRequestLibroValido();
        request.setAutor(""); // libro sin autor: rompe la regla de negocio

        assertThatThrownBy(() -> productoService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("LIBRO debe tener autor");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerPorId: lanza 404 cuando el producto no existe")
    void obtenerPorId_noExiste_lanzaRecursoNoEncontrado() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No existe el producto");
    }
}
