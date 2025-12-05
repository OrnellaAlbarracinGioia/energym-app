package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "clases")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación con Actividad (muchas reservas pertenecen a una actividad)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private LocalDate fecha;

    // Relación con Entrenador (muchas actividades pueden tener el mismo entrenador)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrenador_id", nullable = false)
    private Entrenador entrenador;

    // Relación con Sucursal (muchas actividades pertenecen a una sucursal)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Builder.Default
    @Column(name = "cupos_ocupados", nullable = false)
    private Integer cuposOcupados = 0;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @Builder.Default //Permite que Lombok respete el false, porque sino lo ignora
    @Column(name = "personalizada")
    private Boolean personalizada = false;

}
