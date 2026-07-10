package com.esercizio.travel.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Nessun elemento trovato con id: " + id);
    }
}
