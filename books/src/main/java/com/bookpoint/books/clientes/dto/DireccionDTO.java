package com.bookpoint.books.clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionDTO {

    private Long id;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 200)
    private String calle;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100)
    private String ciudad;

    @Size(max = 100)
    private String region;

    @Size(max = 20)
    private String codigoPostal;
}
