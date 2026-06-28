package com.bookpoint.carrito.service;

import com.bookpoint.carrito.client.ProductoClient;
import com.bookpoint.carrito.dto.CarritoRequestDTO;
import com.bookpoint.carrito.dto.CarritoResponseDTO;
import com.bookpoint.carrito.dto.ItemRequestDTO;
import com.bookpoint.carrito.dto.ItemResponseDTO;
import com.bookpoint.carrito.dto.ProductoDTO;
import com.bookpoint.carrito.exception.RecursoNoEncontradoException;
import com.bookpoint.carrito.model.Carrito;
import com.bookpoint.carrito.model.ItemCarrito;
import com.bookpoint.carrito.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica de negocio del carrito.
 *
 * Lo interesante de este servicio es que, al agregar un item, consulta a
 * ms-productos (via ProductoClient/WebClient) para validar el producto y
 * obtener su titulo y precio reales.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoClient productoClient;

    // ------------------------- Lecturas -------------------------

    public List<CarritoResponseDTO> listarTodos() {
        return carritoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public CarritoResponseDTO obtenerPorId(Long id) {
        return convertirAResponse(buscarCarritoOFallar(id));
    }

    // ------------------------- Escrituras -------------------------

    /** Crea un carrito vacio para un cliente. */
    @Transactional
    public CarritoResponseDTO crear(CarritoRequestDTO dto) {
        Carrito carrito = new Carrito();
        carrito.setClienteId(dto.getClienteId());
        carrito.setFechaCreacion(LocalDateTime.now());

        Carrito guardado = carritoRepository.save(carrito);
        log.info("Carrito creado: id={} para cliente {}", guardado.getId(), guardado.getClienteId());
        return convertirAResponse(guardado);
    }

    /**
     * Agrega un item al carrito.
     * Antes de guardar, consulta a ms-productos para validar el producto y
     * traer su titulo y precio.
     */
    @Transactional
    public CarritoResponseDTO agregarItem(Long carritoId, ItemRequestDTO dto) {
        Carrito carrito = buscarCarritoOFallar(carritoId);

        // Comunicacion remota: validar y obtener datos del producto
        ProductoDTO producto = productoClient.buscarProducto(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException(
                        "El producto " + dto.getProductoId() + " no existe en el catalogo"));

        ItemCarrito item = new ItemCarrito();
        item.setProductoId(producto.getId());
        item.setTituloProducto(producto.getTitulo());
        item.setPrecioUnitario(producto.getPrecio());
        item.setCantidad(dto.getCantidad());
        item.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(dto.getCantidad())));

        carrito.agregarItem(item);
        Carrito guardado = carritoRepository.save(carrito);
        log.info("Item agregado al carrito {}: producto '{}' x{}",
                carritoId, producto.getTitulo(), dto.getCantidad());
        return convertirAResponse(guardado);
    }

    /** Quita un item del carrito. */
    @Transactional
    public CarritoResponseDTO quitarItem(Long carritoId, Long itemId) {
        Carrito carrito = buscarCarritoOFallar(carritoId);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El item " + itemId + " no pertenece al carrito " + carritoId));

        carrito.quitarItem(item);
        Carrito guardado = carritoRepository.save(carrito);
        log.info("Item {} quitado del carrito {}", itemId, carritoId);
        return convertirAResponse(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        Carrito carrito = buscarCarritoOFallar(id);
        carritoRepository.delete(carrito);
        log.info("Carrito eliminado: id={}", id);
    }

    // ------------------------- Apoyo -------------------------

    private Carrito buscarCarritoOFallar(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el carrito con id " + id));
    }

    private CarritoResponseDTO convertirAResponse(Carrito carrito) {
        List<ItemResponseDTO> items = carrito.getItems().stream()
                .map(this::convertirItem)
                .toList();

        // El total del carrito es la suma de los subtotales de cada item
        BigDecimal total = items.stream()
                .map(ItemResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CarritoResponseDTO dto = new CarritoResponseDTO();
        dto.setId(carrito.getId());
        dto.setClienteId(carrito.getClienteId());
        dto.setFechaCreacion(carrito.getFechaCreacion());
        dto.setItems(items);
        dto.setTotal(total);
        return dto;
    }

    private ItemResponseDTO convertirItem(ItemCarrito item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setProductoId(item.getProductoId());
        dto.setTituloProducto(item.getTituloProducto());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setCantidad(item.getCantidad());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
