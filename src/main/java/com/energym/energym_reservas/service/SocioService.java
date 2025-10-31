package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.SocioDTO;
import com.energym.energym_reservas.entity.Socio;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocioService {

    private final SocioRepository socioRepository;

    /**
     * Obtener todos los socios
     */
    @Transactional(readOnly = true)
    public List<SocioDTO> getAllSocios() {
        return socioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un socio por ID
     */
    @Transactional(readOnly = true)
    public SocioDTO getSocioById(Integer id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
        return convertToDTO(socio);
    }

    /**
     * Crear un nuevo socio
     */
    @Transactional
    public SocioDTO createSocio(SocioDTO socioDTO) {
        // Validar que el email no esté registrado
        if (socioRepository.existsByEmail(socioDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + socioDTO.getEmail());
        }

        Socio socio = Socio.builder()
                .nombre(socioDTO.getNombre())
                .email(socioDTO.getEmail())
                .telefono(socioDTO.getTelefono())
                .fechaRegistro(LocalDateTime.now())
                .activo(true)
                .clasesPersonalizadas(0)
                .build();

        Socio savedSocio = socioRepository.save(socio);
        return convertToDTO(savedSocio);
    }

    /**
     * Actualizar un socio existente
     */
    @Transactional
    public SocioDTO updateSocio(Integer id, SocioDTO socioDTO) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        // Validar email único si cambió
        if (!socio.getEmail().equals(socioDTO.getEmail()) &&
                socioRepository.existsByEmail(socioDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + socioDTO.getEmail());
        }

        socio.setNombre(socioDTO.getNombre());
        socio.setEmail(socioDTO.getEmail());
        socio.setTelefono(socioDTO.getTelefono());

        if (socioDTO.getActivo() != null) {
            socio.setActivo(socioDTO.getActivo());
        }

        Socio updatedSocio = socioRepository.save(socio);
        return convertToDTO(updatedSocio);
    }

    /**
     * Eliminar un socio (soft delete - marcar como inactivo)
     */
    @Transactional
    public void deleteSocio(Integer id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        // Soft delete: marcar como inactivo en lugar de eliminar físicamente
        socio.setActivo(false);
        socioRepository.save(socio);
    }

    /**
     * Eliminar permanentemente un socio (hard delete)
     */
    @Transactional
    public void deleteSocioPermanente(Integer id) {
        if (!socioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Socio no encontrado con id: " + id);
        }
        socioRepository.deleteById(id);
    }


    /**
     * Obtener socios activos
     */
    @Transactional(readOnly = true)
    public List<SocioDTO> getSociosActivos() {
        return socioRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private SocioDTO convertToDTO(Socio socio) {
        // Obtener número de reservas activas
        Long totalReservas = socioRepository.countReservasActivasBySocioId(socio.getId());

        return SocioDTO.builder()
                .id(socio.getId())
                .nombre(socio.getNombre())
                .email(socio.getEmail())
                .telefono(socio.getTelefono())
                .fechaRegistro(socio.getFechaRegistro())
                .activo(socio.getActivo())
                .clasesPersonalizadas(socio.getClasesPersonalizadas())
                .totalReservas(totalReservas != null ? totalReservas.intValue() : 0)
                .calificaParaSesionGratuita(socio.getClasesPersonalizadas() >= 10)
                .build();
    }
}
