package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.SucursalRequestDTO;
import com.energym.energym_reservas.dto.response.SucursalResponseDTO;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.SucursalMapper;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SucursalService {
    
    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;
    private final ClaseRepository claseRepository;
    
    /*
     * Crear una nueva Sucursal
     */

    public SucursalResponseDTO crearSucursal(SucursalRequestDTO request) {
        // Validar que no exista una sucursal con el mismo nombre
        if (sucursalRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()) {
            throw new BusinessRuleException("Ya existe una sucursal con ese nombre");
        }
        
        Sucursal sucursal = sucursalMapper.toEntity(request);
        Sucursal savedSucursal = sucursalRepository.save(sucursal);
        return sucursalMapper.toResponseDTO(savedSucursal);
    }
    
    /*
     * Obtener todas las Sucursales
     */
    @Transactional(readOnly = true)
    public List<SucursalResponseDTO> obtenerTodasLasSucursales() {
        List<Sucursal> sucursales = sucursalRepository.findAll();
        return sucursalMapper.toResponseList(sucursales);
    }
    
    /*
     * Obtener sucursal por ID
     */
    @Transactional(readOnly = true)
    public SucursalResponseDTO obtenerSucursalPorId(Integer id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "id", id));
        return sucursalMapper.toResponseDTO(sucursal);
    }
    
    /*
     * Buscar sucursales por nombre
     */
    @Transactional(readOnly = true)
    public List<SucursalResponseDTO> buscarSucursalesPorNombre(String nombre) {
        List<Sucursal> sucursales = sucursalRepository.findByNombreContainingIgnoreCase(nombre);
        return sucursalMapper.toResponseList(sucursales);
    }
    
    /*
     * Buscar sucursales por dirección
     */
    @Transactional(readOnly = true)
    public List<SucursalResponseDTO> buscarSucursalesPorDireccion(String direccion) {
        List<Sucursal> sucursales = sucursalRepository.findByDireccionContainingIgnoreCase(direccion);
        return sucursalMapper.toResponseList(sucursales);
    }
    
    /*
     * Actualizar sucursal
     */
    public SucursalResponseDTO actualizarSucursal(Integer id, SucursalRequestDTO request) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "id", id));
        
        // Validar que el nombre no esté en uso por otra sucursal
        if (!sucursal.getNombre().equals(request.getNombre()) &&
            sucursalRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()) {
            throw new BusinessRuleException("Ya existe una sucursal con ese nombre");

        }

        sucursalMapper.updateFromRequest(request, sucursal);
        Sucursal savedSucursal = sucursalRepository.save(sucursal);

        return sucursalMapper.toResponseDTO(savedSucursal);
    }
    
    /*
     * Eliminar Sucursal
     */
    public void eliminarSucursal(Integer id) {
        if (!sucursalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sucursal", "id", id);
        }

        boolean tieneClases = claseRepository.existsBySucursalId(id);

        if (tieneClases) {
            throw new BusinessRuleException("No es posible eliminar la sucursal porque tiene clases asociadas.");
        }
        sucursalRepository.deleteById(id);
    }

}
