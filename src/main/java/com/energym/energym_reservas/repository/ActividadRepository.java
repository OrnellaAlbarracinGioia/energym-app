package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad,Integer> {
}
