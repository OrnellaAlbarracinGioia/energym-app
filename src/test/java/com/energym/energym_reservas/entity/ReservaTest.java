package com.energym.energym_reservas.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    @DisplayName("Reserva con Estado nulo se le asigna Estado por OnCreate")
    void onCreateAsignarEstadoConfirmadaSiNulo() {
        Reserva reserva = new Reserva();
        reserva.setEstado(null);
        reserva.onCreate();

        assertNotNull(reserva.getFechaCreacion(), "La fecha de creación no puede ser nula");
        assertEquals(Estado.CONFIRMADA, reserva.getEstado(),"El estado debería estár inicializado como CONFIRMADA");
    }

    @Test
    @DisplayName("Reserva con Estado existente no es modificado por OnCreate")
    void onCreateEstadoExistenteNoModificar() {
        Reserva reserva = new Reserva();
        reserva.setEstado(Estado.CANCELADA);
        reserva.onCreate();
        assertEquals(Estado.CANCELADA, reserva.getEstado(), "El estado no deberia modificarse");
        assertNotNull(reserva.getFechaCreacion(), "La fecha de creación debe generarse siempre");
    }
}