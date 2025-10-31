package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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


    @NotNull(message = "La capacidad maxima de la clase es requerida")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer capacidadMaxima;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

}
