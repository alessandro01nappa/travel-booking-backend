package com.esercizio.travel.controllers;

import com.esercizio.travel.entities.Reservation;
import com.esercizio.travel.exceptions.ValidationException;
import com.esercizio.travel.payloads.ReservationDTO;
import com.esercizio.travel.payloads.ReservationResponseDTO;
import com.esercizio.travel.services.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDTO saveReservation(@RequestBody @Validated ReservationDTO body, BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        Reservation saved = reservationService.save(body);
        return new ReservationResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<Reservation> getReservations(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "bookingDate") String orderBy) {
        return reservationService.getAll(page, size, orderBy);
    }

    @GetMapping("/{reservationId}")
    public Reservation getById(@PathVariable UUID reservationId) {
        return reservationService.findById(reservationId);
    }

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable UUID reservationId) {
        reservationService.findByIdAndDelete(reservationId);
    }
}
