package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.SucursalRequestDTO;
import com.energym.energym_reservas.dto.response.SucursalResponseDTO;
import com.energym.energym_reservas.service.SucursalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "API para gestión de las sucursales del gimnasio")

public class SucursalController {
    
    private final SucursalService sucursalService;

    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<SucursalResponseDTO> obtenerSucursalPorId(@PathVariable("id") Integer id) {
        SucursalResponseDTO sucursal = sucursalService.obtenerSucursalPorId(id);
        return ResponseEntity.ok(sucursal);
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<SucursalResponseDTO>> buscarSucursalesPorNombre(@RequestParam("nombre") String nombre) {
        List<SucursalResponseDTO> sucursales = sucursalService.buscarSucursalesPorNombre(nombre);
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/buscar/direccion")
    public ResponseEntity<List<SucursalResponseDTO>> buscarSucursalesPorDireccion(@RequestParam("direccion") String direccion) {
        List<SucursalResponseDTO> sucursales = sucursalService.buscarSucursalesPorDireccion(direccion);
        return ResponseEntity.ok(sucursales);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> actualizarSucursal(@PathVariable("id") Integer id, @Valid @RequestBody SucursalRequestDTO request) {
        SucursalResponseDTO sucursal = sucursalService.actualizarSucursal(id, request);
        return ResponseEntity.ok(sucursal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable("id") Integer id) {
        sucursalService.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
