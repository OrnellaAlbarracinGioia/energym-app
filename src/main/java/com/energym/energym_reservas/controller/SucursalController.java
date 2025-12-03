package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.SucursalRequestDTO;
import com.energym.energym_reservas.dto.response.SucursalResponseDTO;
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
    public ResponseEntity<SucursalResponseDTO> crearSucursal(@Valid @RequestBody SucursalRequestDTO request) {
        try {
            SucursalResponseDTO sucursal = sucursalService.crearSucursal(request);
            return new ResponseEntity<>(sucursal, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // READ - Obtener todas las sucursales
    @GetMapping
    public ResponseEntity<List<SucursalResponseDTO>> obtenerTodasLasSucursales() {
        List<SucursalResponseDTO> sucursales = sucursalService.obtenerTodasLasSucursales();
        return ResponseEntity.ok(sucursales);
    }
    
    // READ - Obtener sucursal por ID
    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> obtenerSucursalPorId(@PathVariable Integer id) {
        try {
            SucursalResponseDTO sucursal = sucursalService.obtenerSucursalPorId(id);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /*
     * Buscar sucursales por nombre
     */
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<SucursalResponseDTO>> buscarSucursalesPorNombre(@RequestParam String nombre) {
        List<SucursalResponseDTO> sucursales = sucursalService.buscarSucursalesPorNombre(nombre);
        return ResponseEntity.ok(sucursales);
    }
    
    /*
     * Buscar sucursales por dirección
    */
    @GetMapping("/buscar/direccion")
    public ResponseEntity<List<SucursalResponseDTO>> buscarSucursalesPorDireccion(@RequestParam String direccion) {
        List<SucursalResponseDTO> sucursales = sucursalService.buscarSucursalesPorDireccion(direccion);
        return ResponseEntity.ok(sucursales);
    }

    /*
     * Actualizar sucursal
    */
    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> actualizarSucursal(@PathVariable Integer id, @Valid @RequestBody SucursalRequestDTO request) {
        try {
            SucursalResponseDTO sucursal = sucursalService.actualizarSucursal(id, request);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /*
     * Eliminar sucursal
     */
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
