package com.energym.energym_reservas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    
    private Integer id;
    
    @NotNull(message = "El ID del socio es obligatorio")
    private Integer socioId;
    
    private String socioNombre; // Para mostrar en respuestas
    
    @NotNull(message = "El ID de la clase es obligatorio")
    private Integer claseId;
    
    private String claseNombre; // Para mostrar en respuestas
    
    private LocalDateTime fechaReserva;
    
    @NotNull(message = "La fecha de la clase es obligatoria")
    private LocalDateTime fechaClase;
    
    @NotNull(message = "El estado es obligatorio")
    private String estado; // CONFIRMADA, CANCELADA, COMPLETADA
    
    private LocalDateTime fechaCancelacion;

    // Información adicional para respuestas
    private Integer cuposDisponibles;
}
