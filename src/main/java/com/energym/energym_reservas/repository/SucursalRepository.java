package com.energym.energym_reservas.repository;

import com.energym.energym_reservas.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    Optional<Sucursal> findByNombreIgnoreCase(String nombre);
    
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);
    
    List<Sucursal> findByDireccionContainingIgnoreCase(String direccion);
}
