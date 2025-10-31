package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="reservas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    // Relación con Clase (muchas reservas pertenecen a una clase)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clase_id", nullable = false)
    private Clase clase;

    @Column(nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false)
    private LocalDateTime fechaClase;

    @Column(name = "estado", nullable = false)
    private String estado; // CONFIRMADA, CANCELADA, COMPLETADA

    private LocalDateTime fechaCancelacion;

    @PrePersist
    protected void onCreate() {
        fechaReserva = LocalDateTime.now();
        if (estado == null) {
            estado = "CONFIRMADA";
        }
    }
}
