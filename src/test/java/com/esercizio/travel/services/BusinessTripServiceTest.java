package com.esercizio.travel.services;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.enums.TripState;
import com.esercizio.travel.exceptions.NotFoundException;
import com.esercizio.travel.payloads.BusinessTripDTO;
import com.esercizio.travel.repositories.BusinessTripRepository;
import com.esercizio.travel.repositories.ReservationRepository;
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
class BusinessTripServiceTest {

    @Mock
    private BusinessTripRepository businessTripRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private BusinessTripService businessTripService;

    @Test
    void saveCreatesTripWithDefaultStatusPlanned() {
        BusinessTripDTO dto = new BusinessTripDTO("Milano", LocalDate.of(2026, 9, 1));
        when(businessTripRepository.save(any(BusinessTrip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BusinessTrip saved = businessTripService.save(dto);

        assertEquals("Milano", saved.getDestination());
        assertEquals(TripState.PLANNED, saved.getStatus());
    }

    @Test
    void findByIdThrowsIfNotFound() {
        UUID id = UUID.randomUUID();
        when(businessTripRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> businessTripService.findById(id));
    }

    @Test
    void updateStatusChangesStatus() {
        UUID id = UUID.randomUUID();
        BusinessTrip trip = new BusinessTrip("Roma", LocalDate.of(2026, 10, 1));
        when(businessTripRepository.findById(id)).thenReturn(Optional.of(trip));
        when(businessTripRepository.save(any(BusinessTrip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BusinessTrip updated = businessTripService.updateStatus(id, TripState.COMPLETED);

        assertEquals(TripState.COMPLETED, updated.getStatus());
    }

    @Test
    void deleteRemovesReservationsBeforeTrip() {
        UUID id = UUID.randomUUID();
        BusinessTrip trip = new BusinessTrip("Roma", LocalDate.of(2026, 10, 1));
        when(businessTripRepository.findById(id)).thenReturn(Optional.of(trip));

        businessTripService.findByIdAndDelete(id);

        verify(reservationRepository).deleteAllByBusinessTrip_Id(id);
        verify(businessTripRepository).delete(trip);
    }
}
