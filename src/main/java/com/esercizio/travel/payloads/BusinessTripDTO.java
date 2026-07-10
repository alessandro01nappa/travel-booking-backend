package com.esercizio.travel.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BusinessTripDTO(
        @NotEmpty(message = "La destinazione e' obbligatoria")
        String destination,

        @NotNull(message = "La data di partenza e' obbligatoria")
        LocalDate departureDate
) {
}
