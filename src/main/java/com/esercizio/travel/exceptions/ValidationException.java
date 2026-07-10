package com.esercizio.travel.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Validazione fallita");
        this.errorMessages = errorMessages;
    }
}
