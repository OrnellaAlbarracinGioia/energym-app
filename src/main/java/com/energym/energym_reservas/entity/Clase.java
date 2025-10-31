package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
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

    @NotBlank(message = "El nombre de la clase es solicitado")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "La capacidad maxima de la clase es requerida")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer capacidadMaxima;

    private DayOfWeek dia;

    @Column(nullable = false)
    private LocalTime horario;

    private String descripcion;

    @NotNull(message = "La duración es obligatoria")
    private Integer duracionMinutos;

    // Relación con Entrenador (muchas clases pueden tener el mismo entrenador)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrenador_id", nullable = false)
    private Entrenador entrenador;

    // Relación con Sucursal (muchas clases pertenecen a una sucursal)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    // Relación con Reservas
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @Transient
    public Integer getCuposDisponibles() {
        long reservasActivas = reservas.stream()
                .filter(r -> r.getEstado() != null && r.getEstado().equals("CONFIRMADA")).count();
        return capacidadMaxima - (int) reservasActivas;
    }
}
