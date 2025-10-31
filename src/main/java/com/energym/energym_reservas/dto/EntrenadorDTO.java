package com.energym.energym_reservas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrenadorDTO {
    
    private Integer id;
    
    @NotBlank(message = "El nombre del entrenador es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El contacto es obligatorio")
    private String contacto;
}
