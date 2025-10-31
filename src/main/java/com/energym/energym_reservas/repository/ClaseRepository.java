package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase,Integer> {

    /**
     * Buscar clases por sucursal
     */
    List<Clase> findBySucursalId(Integer sucursalId);

}
