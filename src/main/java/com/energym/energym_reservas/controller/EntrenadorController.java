package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.EntrenadorDTO;
import com.energym.energym_reservas.service.EntrenadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrenadores")
@Tag(name = "Entrenadores", description = "API para gestión de entrenadores del gimnasio")
@RequiredArgsConstructor
public class EntrenadorController {
    
    private final EntrenadorService entrenadorService;

    @PostMapping
    public ResponseEntity<EntrenadorDTO> crearEntrenador(@Valid @RequestBody EntrenadorDTO entrenadorDTO) {
        try {
            EntrenadorDTO entrenador = entrenadorService.crearEntrenador(entrenadorDTO);
            return new ResponseEntity<>(entrenador, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    

    @GetMapping
    public ResponseEntity<List<EntrenadorDTO>> obtenerTodosLosEntrenadores() {
        List<EntrenadorDTO> entrenadores = entrenadorService.obtenerTodosLosEntrenadores();
        return ResponseEntity.ok(entrenadores);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorDTO> obtenerEntrenadorPorId(@PathVariable Integer id) {
        try {
            EntrenadorDTO entrenador = entrenadorService.obtenerEntrenadorPorId(id);
            return ResponseEntity.ok(entrenador);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<EntrenadorDTO>> buscarEntrenadoresPorNombre(@RequestParam String nombre) {
        List<EntrenadorDTO> entrenadores = entrenadorService.buscarEntrenadoresPorNombre(nombre);
        return ResponseEntity.ok(entrenadores);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorDTO> actualizarEntrenador(
            @PathVariable Integer id, 
            @Valid @RequestBody EntrenadorDTO entrenadorDTO) {
        try {
            EntrenadorDTO entrenador = entrenadorService.actualizarEntrenador(id, entrenadorDTO);
            return ResponseEntity.ok(entrenador);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrenador(@PathVariable Integer id) {
        try {
            entrenadorService.eliminarEntrenador(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
