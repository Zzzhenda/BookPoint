package com.bookpoint.ventas.dto;

import com.bookpoint.ventas.model.MetodoPago;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Datos para registrar una venta: de que pedido y con que forma de pago.
 * El monto NO llega del cliente: se toma del total del pedido.
 */
@Data
public class VentaRequestDTO {

    @NotNull(message = "El id del pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El metodo de pago es obligatorio (EFECTIVO, DEBITO o CREDITO)")
    private MetodoPago metodoPago;
}
