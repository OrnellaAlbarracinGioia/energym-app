package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase,Integer>{
    boolean existsByEntrenadorId(Integer entrenadorId);
    boolean existsBySucursalId(Integer sucursalId);
    boolean existsByActividadId(Integer actividadId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Clase c SET c.cuposOcupados = c.cuposOcupados + 1 " +
            "WHERE c.id = :id AND c.cuposOcupados < c.capacidadMaxima")
    int incrementarOcupados(@Param("id") Integer id);
}

