package com.bookpoint.books.pedidos.dto;

import com.bookpoint.books.pedidos.model.EstadoPedido;
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
public class PedidoDTO {

    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "La sucursal que despacha es obligatoria")
    private Long sucursalId;

    private LocalDateTime fecha;

    private BigDecimal total;

    private EstadoPedido estado;

    @Valid
    @NotEmpty(message = "El pedido debe tener al menos un detalle")
    @Builder.Default
    private List<DetallePedidoDTO> detalles = new ArrayList<>();
}
