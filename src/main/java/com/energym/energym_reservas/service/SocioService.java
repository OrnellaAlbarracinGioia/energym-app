package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.SocioRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.dto.response.SocioResponseDTO;
import com.energym.energym_reservas.entity.Estado;
import com.energym.energym_reservas.entity.Reserva;
import com.energym.energym_reservas.entity.Socio;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ReservaMapper;
import com.energym.energym_reservas.mapper.SocioMapper;
import com.energym.energym_reservas.repository.ReservaRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SocioService {

    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;

    /**
     * Obtener todos los socios
     */
    @Transactional(readOnly = true)
    public List<SocioResponseDTO> getAllSocios() {
        List<Socio> socios = socioRepository.findAll();
        return socioMapper.toResponseList(socios);
    }

    /**
     * Obtener un socio por ID
     */
    @Transactional(readOnly = true)
    public SocioResponseDTO getSocioById(Integer id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
        return socioMapper.toResponseDTO(socio);
    }

    /**
     * Crear un nuevo socio
     */
    public SocioResponseDTO createSocio(SocioRequestDTO request) {
        // Validar que el email no esté registrado
        if (socioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + request.getEmail());
        }

        Socio socio = socioMapper.toEntity(request);
        Socio savedSocio = socioRepository.save(socio);
        return socioMapper.toResponseDTO(savedSocio);

    }

    /**
     * Actualizar un socio existente
     */
    public SocioResponseDTO updateSocio(Integer id, SocioRequestDTO request) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        // Validar email único si cambió
        if (!socio.getEmail().equals(request.getEmail()) &&
                socioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + request.getEmail());
        }

        socioMapper.updateSocioFromRequest(request, socio);

        Socio savedSocio = socioRepository.save(socio);
        return socioMapper.toResponseDTO(savedSocio);
    }

    /**
     * Eliminar un socio (soft delete - marcar como inactivo)
     */
    public void deleteSocio(Integer id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        //Marcar como inactivo en lugar de eliminar físicamente
        socio.setActivo(false);
    }

    /**
     * Eliminar permanentemente un socio
     */
    public void deleteSocioPermanente(Integer id) {
        if (!socioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Socio no encontrado con id: " + id);
        }

        boolean tieneHistorial = reservaRepository.existsBySocioId(id);
        if(tieneHistorial){
            throw new RuntimeException("No es posible eliminar un socio que cuenta con historial de reservas");
        }

        socioRepository.deleteById(id);
    }

    public void beneficioClasePersonalizadaGratuita(Integer id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        Integer clasesActualizadas = socio.getClasesPersonalizadas() + 1;
        socio.setClasesPersonalizadas(clasesActualizadas);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerHistorialAsistencia(Integer socioId) {
        if(!socioRepository.existsById(socioId)){
            throw new ResourceNotFoundException("Socio no encontrado");
        }
        List<Reserva> historial = reservaRepository.findBySocioIdAndEstado(socioId, Estado.COMPLETADA);
        return reservaMapper.toResponseList(historial);
    }

}
