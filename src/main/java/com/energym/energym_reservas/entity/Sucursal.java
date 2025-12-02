package com.energym.energym_reservas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "sucursales")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    // Relación con Clases
    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Clase> clases = new ArrayList<>();
}
