package com.esercizio.travel.controllers;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.enums.TripState;
import com.esercizio.travel.payloads.BusinessTripDTO;
import com.esercizio.travel.services.BusinessTripService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusinessTripController.class)
class BusinessTripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BusinessTripService businessTripService;

    @Test
    void postWithValidBodyReturns201() throws Exception {
        BusinessTripDTO dto = new BusinessTripDTO("Milano", LocalDate.of(2026, 9, 1));
        BusinessTrip saved = new BusinessTrip("Milano", LocalDate.of(2026, 9, 1));
        when(businessTripService.save(dto)).thenReturn(saved);

        mockMvc.perform(post("/businesstrips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void postWithoutDestinationReturns400() throws Exception {
        BusinessTripDTO dto = new BusinessTripDTO("", LocalDate.of(2026, 9, 1));

        mockMvc.perform(post("/businesstrips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchStatusReturnsUpdatedTrip() throws Exception {
        UUID id = UUID.randomUUID();
        BusinessTrip trip = new BusinessTrip("Roma", LocalDate.of(2026, 10, 1));
        trip.setStatus(TripState.COMPLETED);
        when(businessTripService.updateStatus(eq(id), any(TripState.class))).thenReturn(trip);

        mockMvc.perform(patch("/businesstrips/{id}/status", id).param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
