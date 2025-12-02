package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Estado;
import com.energym.energym_reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    Integer countByClaseIdAndEstado(Integer claseId, Estado estado);

    List<Reserva> findBySocioId(Integer socioId);
    
    List<Reserva> findByClaseId(Integer claseId);
    
    List<Reserva> findByEstado(Estado estado);

    @Query("SELECT r FROM Reserva r JOIN FETCH r.socio JOIN FETCH r.clase c JOIN FETCH c.actividad")
    List<Reserva> findAllConRelaciones();

    boolean existsBySocioIdAndClaseIdAndEstado(Integer socioId, Integer claseId, Estado estado);

    Integer countBySocioIdAndEstadoAndClaseFechaBetween(Integer socioId, Estado estado, LocalDate fechaInicio, LocalDate fechaFin);

    List<Reserva> findBySocioIdAndEstado(Integer socioId, Estado estado);
}
