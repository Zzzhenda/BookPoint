package com.bookpoint.books.ventas.dto;

import com.bookpoint.books.ventas.model.TipoDocumento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDTO {

    private Long id;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;

    private Long clienteId;

    private LocalDateTime fecha;

    /** Calculado por el servidor a partir de los detalles. */
    private BigDecimal total;

    @NotNull(message = "El tipo de documento es obligatorio (BOLETA o FACTURA)")
    private TipoDocumento tipoDocumento;

    @Valid
    @NotEmpty(message = "La venta debe tener al menos un detalle")
    @Builder.Default
    private List<DetalleVentaDTO> detalles = new ArrayList<>();
}
