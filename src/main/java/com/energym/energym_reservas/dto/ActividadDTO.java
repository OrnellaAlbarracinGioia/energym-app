package com.energym.energym_reservas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActividadDTO {

    private Integer id;

    @NotBlank(message = "El nombre de la actividad es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "La duración es obligatoria")
    private Integer duracionMinutos;
}
