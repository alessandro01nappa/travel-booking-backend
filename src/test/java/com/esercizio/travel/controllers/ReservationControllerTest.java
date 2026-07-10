package com.esercizio.travel.controllers;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.entities.Reservation;
import com.esercizio.travel.entities.Worker;
import com.esercizio.travel.exceptions.BadRequestException;
import com.esercizio.travel.payloads.ReservationDTO;
import com.esercizio.travel.services.ReservationService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void postWithValidBodyReturns201() throws Exception {
        UUID workerId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        ReservationDTO dto = new ReservationDTO(LocalDate.now(), workerId, tripId, "corridoio");
        Reservation saved = new Reservation(LocalDate.now(), new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com"),
                new BusinessTrip("Milano", LocalDate.of(2026, 9, 1)), "corridoio");
        when(reservationService.save(dto)).thenReturn(saved);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void postWithConflictReturns400() throws Exception {
        UUID workerId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        ReservationDTO dto = new ReservationDTO(LocalDate.now(), workerId, tripId, null);
        when(reservationService.save(dto)).thenThrow(new BadRequestException("gia' prenotato per quella data"));

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postWithoutBookingDateReturns400() throws Exception {
        UUID workerId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();
        ReservationDTO dto = new ReservationDTO(null, workerId, tripId, null);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
