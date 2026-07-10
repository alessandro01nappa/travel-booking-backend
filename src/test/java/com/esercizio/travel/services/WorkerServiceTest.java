package com.esercizio.travel.services;

import com.cloudinary.Cloudinary;
import com.esercizio.travel.entities.Worker;
import com.esercizio.travel.exceptions.BadRequestException;
import com.esercizio.travel.exceptions.NotFoundException;
import com.esercizio.travel.payloads.WorkerDTO;
import com.esercizio.travel.repositories.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private WorkerService workerService;

    @Test
    void saveWorksWhenEmailIsFree() {
        WorkerDTO dto = new WorkerDTO("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        when(workerRepository.existsByEmail(dto.email())).thenReturn(false);
        when(workerRepository.save(any(Worker.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Worker saved = workerService.save(dto);

        assertEquals("mrossi", saved.getUsername());
        assertEquals("Mario", saved.getFirstName());
        assertEquals("Rossi", saved.getLastName());
        assertEquals("mario.rossi@mail.com", saved.getEmail());
    }

    @Test
    void saveThrowsIfEmailAlreadyUsed() {
        WorkerDTO dto = new WorkerDTO("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        when(workerRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> workerService.save(dto));
        verify(workerRepository, never()).save(any());
    }

    @Test
    void findByIdThrowsIfNotFound() {
        UUID id = UUID.randomUUID();
        when(workerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workerService.findById(id));
    }

    @Test
    void updateThrowsIfNewEmailAlreadyUsedByAnotherWorker() {
        UUID id = UUID.randomUUID();
        Worker existing = new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        WorkerDTO dto = new WorkerDTO("mrossi", "Mario", "Rossi", "altra@mail.com");

        when(workerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(workerRepository.existsByEmail("altra@mail.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> workerService.findByIdAndUpdate(id, dto));
    }
}
