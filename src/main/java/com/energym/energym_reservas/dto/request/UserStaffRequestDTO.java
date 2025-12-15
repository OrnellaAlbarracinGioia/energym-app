package com.energym.energym_reservas.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStaffRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotNull
    @NotEmpty(message = "Debe asignar al menos un Rol")
    private Set<String> roles;

    @NotBlank(message = "El campo de nombre es requerido")
    private String name;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
}
