package com.energym.energym_reservas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {

    private Integer id;
    private Integer socioId;
    private String socioNombre; // Para mostrar en respuestas

    private Integer claseId;
    private String actividadNombre;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm") //Este dato pertenece a la Clase a la que se encuentra vinculada la Reserva
    private LocalDateTime horarioClase;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") //Hace referencia a lo que es la fecha en que se creó la Reserva
    private LocalDateTime fechaCreacion;

    private String estado; // CONFIRMADA, CANCELADA, COMPLETADA

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL) //Esta notación permite incluir este atributo solamente si no es null
    private LocalDateTime fechaCancelacion;
}
