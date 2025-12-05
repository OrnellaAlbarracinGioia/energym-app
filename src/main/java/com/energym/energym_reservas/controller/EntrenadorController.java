package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.EntrenadorRequestDTO;
import com.energym.energym_reservas.dto.response.EntrenadorResponseDTO;
import com.energym.energym_reservas.service.EntrenadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrenadores")
@Tag(name = "Entrenadores", description = "API para gestión de entrenadores del gimnasio")
@RequiredArgsConstructor
public class EntrenadorController {
    
    private final EntrenadorService entrenadorService;

   /* @PostMapping
    public ResponseEntity<EntrenadorResponseDTO> crearEntrenador(@Valid @RequestBody EntrenadorRequestDTO request) {
        EntrenadorResponseDTO entrenador = entrenadorService.crearEntrenador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(entrenador);
    }*/

    @GetMapping
    public ResponseEntity<List<EntrenadorResponseDTO>> obtenerTodosLosEntrenadores() {
        List<EntrenadorResponseDTO> entrenadores = entrenadorService.obtenerTodosLosEntrenadores();
        return ResponseEntity.ok(entrenadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorResponseDTO> obtenerEntrenadorPorId(@PathVariable("id") Integer id) {
        EntrenadorResponseDTO entrenador = entrenadorService.obtenerEntrenadorPorId(id);
        return ResponseEntity.ok(entrenador);
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<EntrenadorResponseDTO>> buscarEntrenadoresPorNombre(@RequestParam("nombre") String nombre) {
        List<EntrenadorResponseDTO> entrenadores = entrenadorService.buscarEntrenadoresPorNombre(nombre);
        return ResponseEntity.ok(entrenadores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorResponseDTO> actualizarEntrenador(@PathVariable("id") Integer id, @Valid @RequestBody EntrenadorRequestDTO request) {
        EntrenadorResponseDTO entrenador = entrenadorService.actualizarEntrenador(id, request);
        return ResponseEntity.ok(entrenador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrenador(@PathVariable("id") Integer id) {
        entrenadorService.eliminarEntrenador(id);
        return ResponseEntity.noContent().build();
    }
}
