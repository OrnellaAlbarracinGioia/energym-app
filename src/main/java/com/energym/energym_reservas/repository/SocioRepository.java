package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer> {

}
