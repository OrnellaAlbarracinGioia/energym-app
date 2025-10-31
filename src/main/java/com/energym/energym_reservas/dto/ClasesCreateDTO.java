package com.energym.energym_reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasesCreateDTO {

    private Integer entrenadorId;
    private Integer capacidadMaxima;
    private LocalDate fecha;
    private LocalTime horario;
}
