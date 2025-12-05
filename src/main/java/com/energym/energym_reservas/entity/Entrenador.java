package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "entrenadores")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Entrenador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String contacto;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
