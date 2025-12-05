package com.energym.energym_reservas.dto.response;

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
public class ClaseResponseDTO {
    private Integer id;

    private Integer actividadId;
    private String actividadNombre;

    private LocalDate fecha;
    private LocalTime horario;

    private Integer entrenadorId;
    private String entrenadorNombre;

    private Integer sucursalId;
    private String sucursalNombre;

    private Integer capacidadMaxima;

    private Integer cuposOcupados;

    private Boolean personalizada;
}
