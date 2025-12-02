package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "actividades")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

}
