package com.energym.energym_reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ActividadRequestDTO {

    @NotBlank(message = "El nombre de la actividad es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "La duracion es requerida")
    private Integer duracionMinutos;
}
