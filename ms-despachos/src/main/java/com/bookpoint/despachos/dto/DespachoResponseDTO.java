package com.bookpoint.despachos.dto;

import com.bookpoint.despachos.model.EstadoDespacho;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Datos del despacho que devolvemos al exterior.
 */
@Data
public class DespachoResponseDTO {

    private Long id;
    private Long pedidoId;
    private String direccionEnvio;
    private Long sucursalOrigenId;
    private EstadoDespacho estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;
}
