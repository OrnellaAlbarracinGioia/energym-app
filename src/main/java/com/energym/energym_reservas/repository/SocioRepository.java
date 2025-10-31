package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Estado;
import com.energym.energym_reservas.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer> {

    /**
     * Verificar si existe un socio con un email específico
     */
    boolean existsByEmail(String email);

    /**
     * Buscar socios activos
     */
    List<Socio> findByActivoTrue();


    /**
     * Contar reservas activas de un socio
     */
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.socio.id = :socioId AND r.estado = :estado")
    Integer countReservasActivasBySocioId(@Param("socioId") Integer socioId, @Param("estado") Estado estado);

}
