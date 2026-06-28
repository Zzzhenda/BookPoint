package com.bookpoint.carrito.service;

import com.bookpoint.carrito.TestDataFactory;
import com.bookpoint.carrito.client.ProductoClient;
import com.bookpoint.carrito.dto.CarritoResponseDTO;
import com.bookpoint.carrito.dto.ItemRequestDTO;
import com.bookpoint.carrito.dto.ProductoDTO;
import com.bookpoint.carrito.model.Carrito;
import com.bookpoint.carrito.repository.CarritoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de CarritoService.
 *
 * Mockeamos tanto el repositorio como el ProductoClient (la llamada remota a
 * ms-productos). Asi probamos la logica del carrito sin necesidad de que
 * ms-productos este levantado.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoService - Pruebas Unitarias")
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    @DisplayName("agregarItem: agrega el producto y calcula subtotal y total")
    void agregarItem_productoExiste_calculaTotales() {
        Carrito carrito = TestDataFactory.unCarrito();
        ProductoDTO producto = TestDataFactory.unProductoDTO();
        producto.setPrecio(new BigDecimal("1000"));
        ItemRequestDTO request = TestDataFactory.unItemRequest(producto.getId(), 3);

        when(carritoRepository.findById(carrito.getId())).thenReturn(Optional.of(carrito));
        when(productoClient.buscarProducto(producto.getId())).thenReturn(Optional.of(producto));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        CarritoResponseDTO resultado = carritoService.agregarItem(carrito.getId(), request);

        assertThat(resultado.getItems()).hasSize(1);
        // 1000 * 3 = 3000
        assertThat(resultado.getItems().get(0).getSubtotal()).isEqualByComparingTo("3000");
        assertThat(resultado.getTotal()).isEqualByComparingTo("3000");
    }

    @Test
    @DisplayName("agregarItem: lanza excepcion si el producto no existe en el catalogo")
    void agregarItem_productoNoExiste_lanzaExcepcion() {
        Carrito carrito = TestDataFactory.unCarrito();
        ItemRequestDTO request = TestDataFactory.unItemRequest(999L, 1);

        when(carritoRepository.findById(carrito.getId())).thenReturn(Optional.of(carrito));
        when(productoClient.buscarProducto(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carritoService.agregarItem(carrito.getId(), request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no existe en el catalogo");

        verify(carritoRepository, never()).save(any());
    }
}
