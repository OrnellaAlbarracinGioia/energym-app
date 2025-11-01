package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Estado;
import com.energym.energym_reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    /**
     * Contar reservas CONFIRMADAS para una actividad específica
     * Esto determina si hay cupos disponibles
     */

    Integer countByClaseIdAndEstado(Integer claseId, Estado estado);

    List<Reserva> findBySocioId(Integer socioId);
    
    List<Reserva> findByClaseId(Integer claseId);
    
    List<Reserva> findByEstado(Estado estado);

    boolean existsBySocioIdAndClaseIdAndEstado(Integer socioId, Integer claseId, Estado estado);

    Reserva getReservaBySocioIdAndClaseId(Integer socioId, Integer claseId);
}
