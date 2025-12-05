package com.energym.energym_reservas.listener;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.event.ReservaCompletadaEvent;
import com.energym.energym_reservas.service.ReservaService;
import com.energym.energym_reservas.service.SocioService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@AllArgsConstructor
public class SocioEventListener {

    private ReservaService reservaService;
    private SocioService socioService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservaCompletada(ReservaCompletadaEvent event) {

        procesarBeneficio(event.getIdReserva());
    }

    public void procesarBeneficio(Integer idReserva) {
        ReservaResponseDTO reserva = reservaService.obtenerReservaPorId(idReserva);
        Integer idSocio = reserva.getSocioId();
        Integer reservasCompletadasMensual = reservaService.contarClasesCompletadasEnMes(idSocio);

        if (reservasCompletadasMensual == 10) {
            socioService.beneficioClasePersonalizadaGratuita(idSocio);
        }
    }

}
