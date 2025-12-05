package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="socios")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private String telefono;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    /*Esta forma permite que se detecten no solo el beneficio de 10 actividades completas en un mes,
    sino que puede contemplar otro tipo de factores utilizando la misma lógica
    * */
    @Builder.Default
    @Column(name = "clases_personalizadas")
    private Integer clasesPersonalizadas = 0;

    // Relacion con Reservas
    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
