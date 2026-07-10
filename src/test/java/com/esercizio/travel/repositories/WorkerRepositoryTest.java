package com.esercizio.travel.repositories;

import com.esercizio.travel.entities.Worker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class WorkerRepositoryTest {

    @Autowired
    private WorkerRepository workerRepository;

    @Test
    void existsByEmailFindsSavedWorker() {
        Worker worker = new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com");
        workerRepository.save(worker);

        assertTrue(workerRepository.existsByEmail("mario.rossi@mail.com"));
        assertFalse(workerRepository.existsByEmail("nonesiste@mail.com"));
    }
}
