package com.energym.energym_reservas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocioDTO {
    
    private Integer id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    
    private LocalDateTime fechaRegistro;
    
    private Boolean activo;
    
    private Integer clasesPersonalizadas;
    
    // Para mostrar información adicional en respuestas
    private Integer totalReservas;
    private Boolean calificaParaSesionGratuita; // Si tiene 10+ actividades personalizadas
}
