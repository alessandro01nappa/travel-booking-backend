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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private BusinessTripRepository businessTripRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void saveWorksWhenWorkerIsFreeOnThatDate() {
        UUID workerId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        Worker worker = new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        BusinessTrip trip = new BusinessTrip("Milano", LocalDate.of(2026, 9, 1));
        LocalDate bookingDate = LocalDate.now();
        ReservationDTO dto = new ReservationDTO(bookingDate, workerId, tripId, "finestrino per favore");

        when(workerRepository.findById(workerId)).thenReturn(Optional.of(worker));
        when(businessTripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(reservationRepository.existsByWorkerAndBookingDate(worker, bookingDate)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation saved = reservationService.save(dto);

        assertEquals(worker, saved.getWorker());
        assertEquals(trip, saved.getBusinessTrip());
    }

    @Test
    void saveThrowsIfWorkerAlreadyBookedOnSameRequestDate() {
        UUID workerId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        Worker worker = new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        BusinessTrip trip = new BusinessTrip("Milano", LocalDate.of(2026, 9, 1));
        LocalDate bookingDate = LocalDate.now();
        ReservationDTO dto = new ReservationDTO(bookingDate, workerId, tripId, null);

        when(workerRepository.findById(workerId)).thenReturn(Optional.of(worker));
        when(businessTripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(reservationRepository.existsByWorkerAndBookingDate(worker, bookingDate)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> reservationService.save(dto));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void saveThrowsIfWorkerDoesNotExist() {
        UUID workerId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        ReservationDTO dto = new ReservationDTO(LocalDate.now(), workerId, tripId, null);

        when(workerRepository.findById(workerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservationService.save(dto));
    }
}
