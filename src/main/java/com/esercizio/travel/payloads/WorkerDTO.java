package com.esercizio.travel.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record WorkerDTO(

        @NotEmpty(message = "Lo username e' obbligatorio")
        @Size(max = 30, message = "Lo username non puo' superare i 30 caratteri")
        String username,

        @NotEmpty(message = "Il nome e' obbligatorio")
        @Size(max = 20, message = "Il nome non puo' superare i 20 caratteri")
        String firstName,

        @NotEmpty(message = "Il cognome e' obbligatorio")
        @Size(max = 20, message = "Il cognome non puo' superare i 20 caratteri")
        String lastName,

        @NotEmpty(message = "L'email e' obbligatoria")
        @Email(message = "Formato email non valido")
        String email
) {
}
