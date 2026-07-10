package com.esercizio.travel.services;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.entities.Reservation;
import com.esercizio.travel.entities.Worker;
import com.esercizio.travel.exceptions.BadRequestException;
import com.esercizio.travel.exceptions.NotFoundException;
import com.esercizio.travel.payloads.ReservationDTO;
import com.esercizio.travel.repositories.BusinessTripRepository;
import com.esercizio.travel.repositories.ReservationRepository;
import com.esercizio.travel.repositories.WorkerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final WorkerRepository workerRepository;
    private final BusinessTripRepository businessTripRepository;

    public ReservationService(ReservationRepository reservationRepository, WorkerRepository workerRepository,
                               BusinessTripRepository businessTripRepository) {
        this.reservationRepository = reservationRepository;
        this.workerRepository = workerRepository;
        this.businessTripRepository = businessTripRepository;
    }

    public Reservation save(ReservationDTO body) {
        // check che esistano davvero worker e trip
        Worker worker = workerRepository.findById(body.workerId()).orElseThrow(() -> new NotFoundException(body.workerId()));
        BusinessTrip trip = businessTripRepository.findById(body.tripId()).orElseThrow(() -> new NotFoundException(body.tripId()));

        // no due prenotazioni stesso giorno per lo stesso worker
        if (reservationRepository.existsByWorkerAndBookingDate(worker, body.bookingDate())) {
            throw new BadRequestException("Il worker ha gia' una prenotazione in data " + body.bookingDate());
        }

        Reservation newReservation = new Reservation(body.bookingDate(), worker, trip, body.preferences());
        return reservationRepository.save(newReservation);
    }

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Reservation> getAll(int page, int size, String orderBy) {
        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return reservationRepository.findAll(pageable);
    }

    public void findByIdAndDelete(UUID id) {
        Reservation found = findById(id);
        reservationRepository.delete(found);
    }
}
