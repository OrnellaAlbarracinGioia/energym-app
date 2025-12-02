package com.energym.energym_reservas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ClaseScheduleRequestDTO {

    @NotNull(message = "El entrenador es obligatorio")
    private Integer entrenadorId;

    @NotNull
    @Min(value = 1, message = "La capacidad debe ser positiva")
    private Integer capacidadMaxima;

    @NotNull(message = "La fecha es requerida")
    @Schema(example = "2025-11-01", description = "Fecha de la clase en formato yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "La hora es requerida")
    @Schema(example = "09:30", description = "Hora de inicio de la clase en formato HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;

}
