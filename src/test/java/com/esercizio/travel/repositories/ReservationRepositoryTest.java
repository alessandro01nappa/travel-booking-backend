package com.esercizio.travel.repositories;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.entities.Reservation;
import com.esercizio.travel.entities.Worker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private BusinessTripRepository businessTripRepository;

    @Test
    void existsByWorkerAndBookingDateFindsConflict() {
        Worker worker = workerRepository.save(new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com"));
        BusinessTrip trip = businessTripRepository.save(new BusinessTrip("Milano", LocalDate.of(2026, 9, 1)));
        LocalDate bookingDate = LocalDate.of(2026, 7, 10);
        reservationRepository.save(new Reservation(bookingDate, worker, trip, null));

        assertTrue(reservationRepository.existsByWorkerAndBookingDate(worker, bookingDate));
        assertFalse(reservationRepository.existsByWorkerAndBookingDate(worker, LocalDate.of(2026, 12, 25)));
    }
}
