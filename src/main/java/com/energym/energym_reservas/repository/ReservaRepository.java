package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    /**
     * Contar reservas CONFIRMADAS para una clase específica
     * Esto determina si hay cupos disponibles
     */
    @Query("SELECT COUNT(reserva) FROM Reserva reserva WHERE reserva.clase.id = :claseId AND reserva.estado = 'CONFIRMADA'")
    Integer countReservasConfirmadasByClaseId(@Param("claseId") Integer claseId);

    List<Reserva> findBySocioId(Integer socioId);
    
    List<Reserva> findByClaseId(Integer claseId);
    
    List<Reserva> findByEstado(String estado);
}
