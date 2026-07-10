package com.esercizio.travel.controllers;

import com.esercizio.travel.entities.Worker;
import com.esercizio.travel.exceptions.NotFoundException;
import com.esercizio.travel.payloads.WorkerDTO;
import com.esercizio.travel.services.WorkerService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkerController.class)
class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkerService workerService;

    @Test
    void postWithValidBodyReturns201() throws Exception {
        WorkerDTO dto = new WorkerDTO("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        Worker saved = new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        when(workerService.save(dto)).thenReturn(saved);

        mockMvc.perform(post("/workers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void postWithEmptyUsernameReturns400() throws Exception {
        WorkerDTO dto = new WorkerDTO("", "Mario", "Rossi", "mario.rossi@mail.com");

        mockMvc.perform(post("/workers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByIdNotFoundReturns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(workerService.findById(id)).thenThrow(new NotFoundException(id));

        mockMvc.perform(get("/workers/{id}", id))
                .andExpect(status().isNotFound());
    }
}
