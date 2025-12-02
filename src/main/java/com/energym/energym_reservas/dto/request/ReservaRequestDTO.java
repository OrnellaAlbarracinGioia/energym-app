package com.energym.energym_reservas.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "El ID del socio es obligatorio")
    private Integer socioId;

    @NotNull(message = "El ID de la clase es obligatorio")
    private Integer claseId;
}
