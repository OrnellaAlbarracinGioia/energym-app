package com.energym.energym_reservas.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    void onCreate_asignarEstadoConfirmada_siNulo() {
        Reserva reserva = new Reserva();
        reserva.setEstado(null);
        reserva.onCreate();

        assertNotNull(reserva.getFechaCreacion(), "La fecha de creación no puede ser nula");
        assertEquals(Estado.CONFIRMADA, reserva.getEstado(),"El estado debería estár inicializado como CONFIRMADA");
    }

    @Test
    void onCreate_estadoExistente_noModificar() {
        Reserva reserva = new Reserva();
        reserva.setEstado(Estado.CANCELADA);
        reserva.onCreate();
        assertEquals(Estado.CANCELADA, reserva.getEstado(), "El estado no deberia modificarse");
        assertNotNull(reserva.getFechaCreacion(), "La fecha de creación debe generarse siempre");
    }
}