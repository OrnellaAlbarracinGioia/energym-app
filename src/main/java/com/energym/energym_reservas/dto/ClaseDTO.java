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
public class ClaseDTO {

    private Integer id;

    @NotBlank(message = "El nombre de la clase es obligatorio")
    private String nombre;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidadMaxima;

    private DayOfWeek dia;

    @NotNull(message = "El horario es obligatorio")
    private LocalTime horario;

    private String descripcion;

    @NotNull(message = "La duración es obligatoria")
    private Integer duracionMinutos;

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Integer entrenadorId;

    private String entrenadorNombre; // Para mostrar en respuestas

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Integer sucursalId;

    private String sucursalNombre; // Para mostrar en respuestas

    private Integer cuposDisponibles; // Calculado
}
