package com.energym.energym_reservas.listener;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.event.ReservaCompletadaEvent;
import com.energym.energym_reservas.service.ReservaService;
import com.energym.energym_reservas.service.SocioService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class SocioEventListener {

    private ReservaService reservaService;
    private SocioService socioService;

    @EventListener
    public void handleReservaCompletada(ReservaCompletadaEvent event) {

        ReservaResponseDTO reserva = reservaService.obtenerReservaPorId(event.getIdReserva());

        Integer idSocio = reserva.getSocioId();

        Integer reservasCompletadasMensual = reservaService.contarClasesCompletadasEnMes(idSocio);

        if (reservasCompletadasMensual == 10) {
            socioService.beneficioClasePersonalizadaGratuita(idSocio);
        }
    }

}
