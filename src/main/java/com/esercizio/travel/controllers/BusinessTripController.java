package com.esercizio.travel.controllers;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.enums.TripState;
import com.esercizio.travel.exceptions.ValidationException;
import com.esercizio.travel.payloads.BusinessTripDTO;
import com.esercizio.travel.payloads.BusinessTripResponseDTO;
import com.esercizio.travel.services.BusinessTripService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/businesstrips")
public class BusinessTripController {
    private final BusinessTripService businessTripService;

    public BusinessTripController(BusinessTripService businessTripService) {
        this.businessTripService = businessTripService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessTripResponseDTO saveTrip(@RequestBody @Validated BusinessTripDTO body, BindingResult validResult) {
        checkValidation(validResult);
        BusinessTrip saved = businessTripService.save(body);
        return new BusinessTripResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<BusinessTrip> getTrips(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "departureDate") String orderBy) {
        return businessTripService.getAll(page, size, orderBy);
    }

    @GetMapping("/{tripId}")
    public BusinessTrip getById(@PathVariable UUID tripId) {
        return businessTripService.findById(tripId);
    }

    @PutMapping("/{tripId}")
    public BusinessTrip updateTrip(@PathVariable UUID tripId, @RequestBody @Validated BusinessTripDTO body, BindingResult validResult) {
        checkValidation(validResult);
        return businessTripService.findByIdAndUpdate(tripId, body);
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(@PathVariable UUID tripId) {
        businessTripService.findByIdAndDelete(tripId);
    }

    // solo per cambiare stato, tipo in programma -> completato
    @PatchMapping("/{tripId}/status")
    public BusinessTrip changeStatus(@PathVariable UUID tripId, @RequestParam TripState status) {
        return businessTripService.updateStatus(tripId, status);
    }

    private void checkValidation(BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
    }
}
