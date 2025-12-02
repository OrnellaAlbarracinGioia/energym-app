package com.energym.energym_reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalRequestDTO {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
}
