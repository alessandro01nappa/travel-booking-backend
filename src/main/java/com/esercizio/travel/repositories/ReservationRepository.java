package com.esercizio.travel.repositories;

import com.esercizio.travel.entities.Reservation;
import com.esercizio.travel.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    // per il check anti doppia prenotazione stesso giorno
    boolean existsByWorkerAndBookingDate(Worker worker, LocalDate bookingDate);
}
