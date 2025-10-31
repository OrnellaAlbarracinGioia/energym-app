package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "entrenadores")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del entrenador es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El contacto es obligatorio")
    private String contacto;

}
