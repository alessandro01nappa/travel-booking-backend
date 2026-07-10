package com.esercizio.travel.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationDTO(
        @NotNull(message = "La data della richiesta e' obbligatoria")
        LocalDate bookingDate,

        @NotNull(message = "L'id del worker e' obbligatorio")
        UUID workerId,

        @NotNull(message = "L'id del viaggio e' obbligatorio")
        UUID tripId,

        String preferences
) {
}
