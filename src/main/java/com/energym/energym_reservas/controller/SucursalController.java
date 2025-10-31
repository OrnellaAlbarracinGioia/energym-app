package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.SucursalDTO;
import com.energym.energym_reservas.service.SucursalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "API para gestión de las sucursales del gimnasio")

public class SucursalController {
    
    private final SucursalService sucursalService;
    
    // CREATE - Crear nueva sucursal
    @PostMapping
    public ResponseEntity<SucursalDTO> crearSucursal(@Valid @RequestBody SucursalDTO sucursalDTO) {
        try {
            SucursalDTO sucursal = sucursalService.crearSucursal(sucursalDTO);
            return new ResponseEntity<>(sucursal, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // READ - Obtener todas las sucursales
    @GetMapping
    public ResponseEntity<List<SucursalDTO>> obtenerTodasLasSucursales() {
        List<SucursalDTO> sucursales = sucursalService.obtenerTodasLasSucursales();
        return ResponseEntity.ok(sucursales);
    }
    
    // READ - Obtener sucursal por ID
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> obtenerSucursalPorId(@PathVariable Integer id) {
        try {
            SucursalDTO sucursal = sucursalService.obtenerSucursalPorId(id);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    // READ - Buscar sucursales por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<SucursalDTO>> buscarSucursalesPorNombre(@RequestParam String nombre) {
        List<SucursalDTO> sucursales = sucursalService.buscarSucursalesPorNombre(nombre);
        return ResponseEntity.ok(sucursales);
    }
    
    // READ - Buscar sucursales por dirección
    @GetMapping("/buscar/direccion")
    public ResponseEntity<List<SucursalDTO>> buscarSucursalesPorDireccion(@RequestParam String direccion) {
        List<SucursalDTO> sucursales = sucursalService.buscarSucursalesPorDireccion(direccion);
        return ResponseEntity.ok(sucursales);
    }
    
    // UPDATE - Actualizar sucursal
    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> actualizarSucursal(
            @PathVariable Integer id, 
            @Valid @RequestBody SucursalDTO sucursalDTO) {
        try {
            SucursalDTO sucursal = sucursalService.actualizarSucursal(id, sucursalDTO);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    // DELETE - Eliminar sucursal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Integer id) {
        try {
            sucursalService.eliminarSucursal(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
