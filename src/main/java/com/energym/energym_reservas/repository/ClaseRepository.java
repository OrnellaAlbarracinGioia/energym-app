package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase,Integer>{
    boolean existsEntrenadorById(Integer entrenadorId);
    boolean existsBySucursalId(Integer sucursalId);
}

