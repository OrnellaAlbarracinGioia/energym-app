package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Integer> {
    
    // Métodos de consulta personalizados
    Optional<Entrenador> findByNombre(String nombre);
    List<Entrenador> findByNombreContainingIgnoreCase(String nombre);

    Optional<Entrenador> findByContacto(String contacto);
}
