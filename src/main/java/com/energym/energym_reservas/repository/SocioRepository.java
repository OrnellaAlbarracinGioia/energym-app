package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer> {

    boolean existsByEmail(String email);

    List<Socio> findByActivoTrue();

}
