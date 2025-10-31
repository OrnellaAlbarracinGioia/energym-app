package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.EntrenadorDTO;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.repository.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EntrenadorService {
    
    private final EntrenadorRepository entrenadorRepository;
    
    // CREATE
    public EntrenadorDTO crearEntrenador(EntrenadorDTO entrenadorDTO) {
        // Validar que no exista un entrenador con el mismo contacto
        if (entrenadorRepository.findByContacto(entrenadorDTO.getContacto()).isPresent()) {
            throw new RuntimeException("Ya existe un entrenador con ese contacto");
        }
        
        Entrenador entrenador = Entrenador.builder()
                .nombre(entrenadorDTO.getNombre())
                .contacto(entrenadorDTO.getContacto())
                .build();
        
        Entrenador entrenadorGuardado = entrenadorRepository.save(entrenador);
        return convertirADTO(entrenadorGuardado);
    }
    
    // READ - Obtener todos los entrenadores
    @Transactional(readOnly = true)
    public List<EntrenadorDTO> obtenerTodosLosEntrenadores() {
        return entrenadorRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // READ - Obtener entrenador por ID
    @Transactional(readOnly = true)
    public EntrenadorDTO obtenerEntrenadorPorId(Integer id) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con id: " + id));
        return convertirADTO(entrenador);
    }

    // READ - Buscar entrenadores por nombre
    @Transactional(readOnly = true)
    public List<EntrenadorDTO> buscarEntrenadoresPorNombre(String nombre) {
        return entrenadorRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // UPDATE - Actualizar entrenador
    public EntrenadorDTO actualizarEntrenador(Integer id, EntrenadorDTO entrenadorDTO) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con id: " + id));
        
        // Validar que el contacto no esté en uso por otro entrenador
        if (entrenadorDTO.getContacto() != null && 
            !entrenadorDTO.getContacto().equals(entrenador.getContacto())) {
            entrenadorRepository.findByContacto(entrenadorDTO.getContacto())
                    .ifPresent(e -> {
                        if (!e.getId().equals(id)) {
                            throw new RuntimeException("Ya existe un entrenador con ese contacto");
                        }
                    });
        }
        
        if (entrenadorDTO.getNombre() != null) {
            entrenador.setNombre(entrenadorDTO.getNombre());
        }
        
        if (entrenadorDTO.getContacto() != null) {
            entrenador.setContacto(entrenadorDTO.getContacto());
        }
        
        Entrenador entrenadorActualizado = entrenadorRepository.save(entrenador);
        return convertirADTO(entrenadorActualizado);
    }
    
    // DELETE
    public void eliminarEntrenador(Integer id) {
        if (!entrenadorRepository.existsById(id)) {
            throw new RuntimeException("Entrenador no encontrado con id: " + id);
        }
        entrenadorRepository.deleteById(id);
    }
    
    // Metodo auxiliar para convertir entidad a DTO
    private EntrenadorDTO convertirADTO(Entrenador entrenador) {
        return EntrenadorDTO.builder()
                .id(entrenador.getId())
                .nombre(entrenador.getNombre())
                .contacto(entrenador.getContacto())
                .build();
    }
}
