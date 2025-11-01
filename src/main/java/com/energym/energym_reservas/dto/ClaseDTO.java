package com.energym.energym_reservas.dto;

import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private Integer actividadId;

    @Schema(example = "2025-11-01", description = "Fecha de la clase en formato yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @Schema(example = "09:30:00", description = "Hora de inicio de la clase en formato HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime horario;

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Integer entrenadorId;

    @NotNull(message = "El ID de la sucursal es obligatorio")

    private Integer sucursalId;

    @NotNull(message = "La capacidad maxima de la clase es requerida")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacidadMaxima;

}
