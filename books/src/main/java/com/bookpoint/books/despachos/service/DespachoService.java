package com.bookpoint.books.despachos.service;

import com.bookpoint.books.despachos.dto.DespachoDTO;
import com.bookpoint.books.despachos.model.Despacho;
import com.bookpoint.books.despachos.model.EstadoDespacho;
import com.bookpoint.books.despachos.repository.DespachoRepository;
import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import com.bookpoint.books.pedidos.client.PedidoClient;
import com.bookpoint.books.pedidos.model.EstadoPedido;
import com.bookpoint.books.sucursales.repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica de despachos.
 * Reglas de negocio:
 *  - No se puede crear despacho para un pedido inexistente (se valida via WebClient).
 *  - No se permiten dos despachos para el mismo pedido.
 *  - Solo se pasa a EN_RUTA desde PREPARANDO.
 *  - Solo se pasa a ENTREGADO desde EN_RUTA.
 *  - Al ENTREGAR, se llama al microservicio de pedidos via WebClient para
 *    sincronizar el estado del pedido (cumple IE 2.4.x de la rubrica).
 */
@Service
public class DespachoService {

    private static final Logger log = LoggerFactory.getLogger(DespachoService.class);

    private final DespachoRepository repository;
    private final PedidoClient pedidoClient;
    private final SucursalRepository sucursalRepository;

    public DespachoService(DespachoRepository repository,
                           PedidoClient pedidoClient,
                           SucursalRepository sucursalRepository) {
        this.repository = repository;
        this.pedidoClient = pedidoClient;
        this.sucursalRepository = sucursalRepository;
    }

    public List<DespachoDTO> listar() {
        log.info("Listando despachos");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    public DespachoDTO buscarPorId(Long id) {
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Despacho con id " + id + " no encontrado")));
    }

    public DespachoDTO crear(DespachoDTO dto) {
        log.info("Creando despacho para pedido {}", dto.getPedidoId());
        // Verifica que el pedido exista consultando el microservicio remoto.
        pedidoClient.obtener(dto.getPedidoId());
        if (!sucursalRepository.existsById(dto.getSucursalOrigenId())) {
            throw new RecursoNoEncontradoException(
                    "Sucursal de origen " + dto.getSucursalOrigenId() + " no existe");
        }
        if (repository.findByPedidoId(dto.getPedidoId()).isPresent()) {
            throw new ReglaNegocioException(
                    "Ya existe un despacho para el pedido " + dto.getPedidoId());
        }
        Despacho d = Despacho.builder()
                .pedidoId(dto.getPedidoId())
                .sucursalOrigenId(dto.getSucursalOrigenId())
                .direccionDestino(dto.getDireccionDestino())
                .ciudadDestino(dto.getCiudadDestino())
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoDespacho.PREPARANDO)
                .observaciones(dto.getObservaciones()).build();
        return aDTO(repository.save(d));
    }

    public DespachoDTO marcarEnRuta(Long id) {
        log.info("Marcando despacho {} EN_RUTA", id);
        Despacho d = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Despacho con id " + id + " no encontrado"));
        if (d.getEstado() != EstadoDespacho.PREPARANDO) {
            throw new ReglaNegocioException(
                    "Solo se puede pasar a EN_RUTA desde PREPARANDO. Estado actual: " + d.getEstado());
        }
        d.setEstado(EstadoDespacho.EN_RUTA);
        return aDTO(repository.save(d));
    }

    public DespachoDTO marcarEntregado(Long id) {
        log.info("Marcando despacho {} ENTREGADO", id);
        Despacho d = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Despacho con id " + id + " no encontrado"));
        if (d.getEstado() != EstadoDespacho.EN_RUTA) {
            throw new ReglaNegocioException(
                    "Solo se puede entregar desde EN_RUTA. Estado actual: " + d.getEstado());
        }
        d.setEstado(EstadoDespacho.ENTREGADO);
        d.setFechaEntrega(LocalDateTime.now());
        Despacho guardado = repository.save(d);
        // Sincroniza el estado del pedido a traves del microservicio de pedidos.
        pedidoClient.cambiarEstado(guardado.getPedidoId(), EstadoPedido.ENTREGADO);
        return aDTO(guardado);
    }

    public DespachoDTO cancelar(Long id) {
        log.info("Cancelando despacho {}", id);
        Despacho d = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Despacho con id " + id + " no encontrado"));
        if (d.getEstado() == EstadoDespacho.ENTREGADO) {
            throw new ReglaNegocioException("No se puede cancelar un despacho ya entregado");
        }
        d.setEstado(EstadoDespacho.CANCELADO);
        return aDTO(repository.save(d));
    }

    public void eliminar(Long id) {
        log.info("Eliminando despacho {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Despacho con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    private DespachoDTO aDTO(Despacho d) {
        return DespachoDTO.builder().id(d.getId()).pedidoId(d.getPedidoId())
                .sucursalOrigenId(d.getSucursalOrigenId())
                .direccionDestino(d.getDireccionDestino()).ciudadDestino(d.getCiudadDestino())
                .fechaCreacion(d.getFechaCreacion()).fechaEntrega(d.getFechaEntrega())
                .estado(d.getEstado()).observaciones(d.getObservaciones()).build();
    }
}
