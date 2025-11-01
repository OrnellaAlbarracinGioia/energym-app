package com.energym.energym_reservas.dto;

import com.energym.energym_reservas.entity.Estado;
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
    
    private LocalDateTime fecha;

    private String estado; // CONFIRMADA, CANCELADA, COMPLETADA
    
    private LocalDateTime fechaCancelacion;


}
