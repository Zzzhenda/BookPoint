package com.bookpoint.inventario.dto;

import lombok.Data;

/**
 * Datos de stock que devolvemos al exterior.
 * Incluye "bajoStock": un campo calculado que indica si ya hay que reponer.
 */
@Data
public class InventarioResponseDTO {

    private Long id;
    private Long productoId;
    private Long sucursalId;
    private Integer cantidad;
    private Integer stockMinimo;
    private Boolean bajoStock;
}
