package com.energym.energym_reservas.exception;

public class CapacidadLlenaException extends RuntimeException {
    private final String claseNombre;
    private final Integer cuposDisponibles;
    
    public CapacidadLlenaException(String mensaje, String claseNombre, Integer cuposDisponibles) {
        super(mensaje);
        this.claseNombre = claseNombre;
        this.cuposDisponibles = cuposDisponibles;
    }
    
    public String getClaseNombre() {
        return claseNombre;
    }
    
    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }
}
