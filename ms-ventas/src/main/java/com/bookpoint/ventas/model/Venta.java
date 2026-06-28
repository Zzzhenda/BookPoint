package com.bookpoint.ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Venta: la boleta generada para un pedido.
 *
 * pedidoId referencia al pedido en ms-pedidos. Es unico: un pedido se factura
 * una sola vez.
 */
@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false, unique = true)
    private Long pedidoId;

    /** Numero de boleta legible, ej: BOL-000001. */
    @Column(name = "numero_boleta", nullable = false, unique = true, length = 20)
    private String numeroBoleta;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 10)
    private MetodoPago metodoPago;
}
