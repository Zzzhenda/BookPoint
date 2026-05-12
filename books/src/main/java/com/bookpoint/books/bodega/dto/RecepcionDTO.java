package com.bookpoint.books.bodega.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecepcionDTO {

    private Long id;

    @NotNull(message = "El id del proveedor es obligatorio")
    private Long proveedorId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Size(max = 200)
    private String observaciones;

    @Valid
    @NotEmpty(message = "Una recepcion debe tener al menos un detalle")
    @Builder.Default
    private List<DetalleRecepcionDTO> detalles = new ArrayList<>();
}
