package com.bookpoint.ventas.dto;

import com.bookpoint.ventas.model.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Datos de la venta/boleta que devolvemos al exterior.
 */
@Data
public class VentaResponseDTO {

    private Long id;
    private Long pedidoId;
    private String numeroBoleta;
    private LocalDateTime fecha;
    private BigDecimal montoTotal;
    private MetodoPago metodoPago;
}
