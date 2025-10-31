package com.energym.energym_reservas.dto;

import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {

    private Integer id;

    @NotNull(message = "El ID de la actividad es obligatorio")
    private Actividad actividad;

    private DayOfWeek dia;

    private LocalTime horario;

    private LocalDateTime fecha;

    @NotNull(message = "El ID de la actividad es obligatorio")
    private Entrenador entrenador;

    @NotNull(message = "El ID de la actividad es obligatorio")
    private Sucursal sucursal;
}
