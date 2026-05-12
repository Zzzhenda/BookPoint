package com.bookpoint.books.clientes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email no valido")
    private String email;

    @NotBlank(message = "La password es obligatoria")
    @Size(min = 6, max = 100)
    private String password;

    @Size(max = 20)
    private String telefono;

    private Boolean activo;

    @Valid
    @Builder.Default
    private List<DireccionDTO> direcciones = new ArrayList<>();
}
