package com.bookpoint.books.usuarios.dto;

import com.bookpoint.books.usuarios.model.Rol;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email no valido")
    private String email;

    @NotBlank(message = "La password es obligatoria")
    @Size(min = 6, max = 100, message = "La password debe tener entre 6 y 100 caracteres")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    private Long sucursalId;

    private Boolean activo;
}
