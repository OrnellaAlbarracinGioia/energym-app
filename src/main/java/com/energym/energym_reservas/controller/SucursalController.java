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

    @PostMapping
    public ResponseEntity<SucursalResponseDTO> crearSucursal(@Valid @RequestBody SucursalRequestDTO request) {
        SucursalResponseDTO sucursal = sucursalService.crearSucursal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursal);
    }

    @GetMapping
    public ResponseEntity<List<SucursalResponseDTO>> obtenerTodasLasSucursales() {
        List<SucursalResponseDTO> sucursales = sucursalService.obtenerTodasLasSucursales();
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> obtenerSucursalPorId(@PathVariable Integer id) {
        SucursalResponseDTO sucursal = sucursalService.obtenerSucursalPorId(id);
        return ResponseEntity.ok(sucursal);
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<SucursalResponseDTO>> buscarSucursalesPorNombre(@RequestParam String nombre) {
        List<SucursalResponseDTO> sucursales = sucursalService.buscarSucursalesPorNombre(nombre);
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/buscar/direccion")
    public ResponseEntity<List<SucursalResponseDTO>> buscarSucursalesPorDireccion(@RequestParam String direccion) {
        List<SucursalResponseDTO> sucursales = sucursalService.buscarSucursalesPorDireccion(direccion);
        return ResponseEntity.ok(sucursales);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> actualizarSucursal(@PathVariable Integer id, @Valid @RequestBody SucursalRequestDTO request) {
        SucursalResponseDTO sucursal = sucursalService.actualizarSucursal(id, request);
        return ResponseEntity.ok(sucursal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Integer id) {
        sucursalService.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
