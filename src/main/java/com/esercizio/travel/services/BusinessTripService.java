package com.esercizio.travel.services;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.enums.TripState;
import com.esercizio.travel.exceptions.NotFoundException;
import com.esercizio.travel.payloads.BusinessTripDTO;
import com.esercizio.travel.repositories.BusinessTripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BusinessTripService {
    private final BusinessTripRepository businessTripRepository;

    public BusinessTripService(BusinessTripRepository businessTripRepository) {
        this.businessTripRepository = businessTripRepository;
    }

    public BusinessTrip save(BusinessTripDTO body) {
        BusinessTrip newTrip = new BusinessTrip(body.destination(), body.departureDate());
        return businessTripRepository.save(newTrip);
    }

    public BusinessTrip findById(UUID id) {
        return businessTripRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<BusinessTrip> getAll(int page, int size, String orderBy) {
        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return businessTripRepository.findAll(pageable);
    }

    public BusinessTrip findByIdAndUpdate(UUID id, BusinessTripDTO body) {
        BusinessTrip found = findById(id);
        found.setDestination(body.destination());
        found.setDepartureDate(body.departureDate());
        return businessTripRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        BusinessTrip found = findById(id);
        businessTripRepository.delete(found);
    }

    public BusinessTrip updateStatus(UUID id, TripState status) {
        BusinessTrip found = findById(id);
        found.setStatus(status);
        return businessTripRepository.save(found);
    }
}
