package com.bookpoint.books.proveedores.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String nombre;

    @Size(max = 20)
    private String rut;

    @Size(max = 100)
    private String contactoNombre;

    @Email(message = "Email no valido")
    @Size(max = 100)
    private String contactoEmail;

    @Size(max = 20)
    private String telefono;

    private Boolean activo;
}
