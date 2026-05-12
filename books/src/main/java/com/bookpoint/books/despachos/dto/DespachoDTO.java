package com.bookpoint.books.despachos.dto;

import com.bookpoint.books.despachos.model.EstadoDespacho;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespachoDTO {

    private Long id;

    @NotNull(message = "El id del pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "La sucursal de origen es obligatoria")
    private Long sucursalOrigenId;

    @NotBlank(message = "La direccion de destino es obligatoria")
    @Size(max = 200)
    private String direccionDestino;

    @NotBlank(message = "La ciudad de destino es obligatoria")
    @Size(max = 100)
    private String ciudadDestino;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;

    private EstadoDespacho estado;

    @Size(max = 200)
    private String observaciones;
}
