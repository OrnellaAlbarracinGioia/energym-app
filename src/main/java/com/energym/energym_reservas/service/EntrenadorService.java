package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.EntrenadorRequestDTO;
import com.energym.energym_reservas.dto.response.EntrenadorResponseDTO;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.EntrenadorMapper;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EntrenadorService {
    
    private final EntrenadorRepository entrenadorRepository;
    private final EntrenadorMapper entrenadorMapper;
    private final ClaseRepository claseRepository;
    
    /*
     * Obtener todos los entrenadores
     */

    @Transactional(readOnly = true)
    public List<EntrenadorResponseDTO> obtenerTodosLosEntrenadores() {
        List<Entrenador> entrenadores = entrenadorRepository.findAll();
        return entrenadorMapper.toResponseList(entrenadores);
    }
    
    /*
     * Obtener entrenador por ID
     */
    @Transactional(readOnly = true)
    public EntrenadorResponseDTO obtenerEntrenadorPorId(Integer id) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador", "id", id));
        return entrenadorMapper.toResponseDTO(entrenador);
    }

    /*
     * Buscar entrenadores por nombre
     */
    @Transactional(readOnly = true)
    public List<EntrenadorResponseDTO> buscarEntrenadoresPorNombre(String nombre) {
        List<Entrenador>  entrenadores = entrenadorRepository.findByNombreContainingIgnoreCase(nombre);
        return entrenadorMapper.toResponseList(entrenadores);
    }
    
    /*
     * Actualizar entrenador
     */
    public EntrenadorResponseDTO actualizarEntrenador(Integer id, EntrenadorRequestDTO request) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador", "id", id));
        
        // Validar que el contacto no esté en uso por otro entrenador
        if (!entrenador.getContacto().equals(request.getContacto()) &&
            entrenadorRepository.findByContacto(request.getContacto()).isPresent()) {
                throw new BusinessRuleException("Ya existe un entrenador con ese contacto");
        }

        entrenadorMapper.updateFromRequest(request, entrenador);
        Entrenador savedEntrenador =  entrenadorRepository.save(entrenador);

        return entrenadorMapper.toResponseDTO(savedEntrenador);
    }
    
    /*
     * Eliminar un Entrenador
     */
    public void eliminarEntrenador(Integer id) {
        if (!entrenadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entrenador", "id", id);
        }

        boolean tieneClases = claseRepository.existsByEntrenadorId(id);
        if(tieneClases) {
            throw new BusinessRuleException("No es posible eliminar un entrenador que cuenta con clases asignadas");
        }
        entrenadorRepository.deleteById(id);
    }
}
