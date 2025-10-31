package com.energym.energym_reservas.dto;

import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {

    private Integer id;

    @NotNull(message = "El ID de la actividad es obligatorio")
    private Actividad actividad;

    private LocalTime horario;

    private LocalDate fecha;

    @NotNull(message = "El ID de la actividad es obligatorio")
    private Entrenador entrenador;

    @NotNull(message = "El ID de la actividad es obligatorio")
    private Sucursal sucursal;

    @NotNull(message = "La capacidad maxima de la clase es requerida")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidadMaxima;

}
