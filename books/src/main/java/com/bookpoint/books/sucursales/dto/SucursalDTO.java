package com.bookpoint.books.sucursales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucursalDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100)
    private String ciudad;

    @Size(max = 200)
    private String direccion;

    @Size(max = 20)
    private String telefono;

    @Size(max = 100)
    private String horario;

    private Boolean activa;
}
