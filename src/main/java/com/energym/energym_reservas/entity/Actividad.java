package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "actividades")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la actividad es solicitado")
    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @NotNull(message = "La duración es obligatoria")
    private Integer duracionMinutos;

}
