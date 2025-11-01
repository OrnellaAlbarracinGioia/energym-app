package com.energym.energym_reservas.event;

import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReservaCompletadaEvent extends ApplicationEvent {

    private final Integer idReserva;

    public ReservaCompletadaEvent(Object source, Integer idReserva) {
        super(source);
        this.idReserva = idReserva;
    }

}
