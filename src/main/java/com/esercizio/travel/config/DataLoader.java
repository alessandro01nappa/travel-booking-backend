package com.esercizio.travel.config;

import com.esercizio.travel.entities.BusinessTrip;
import com.esercizio.travel.entities.Reservation;
import com.esercizio.travel.entities.Worker;
import com.esercizio.travel.enums.TripState;
import com.esercizio.travel.repositories.BusinessTripRepository;
import com.esercizio.travel.repositories.ReservationRepository;
import com.esercizio.travel.repositories.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {
    private final WorkerRepository workerRepository;
    private final BusinessTripRepository businessTripRepository;
    private final ReservationRepository reservationRepository;

    public DataLoader(WorkerRepository workerRepository, BusinessTripRepository businessTripRepository,
                       ReservationRepository reservationRepository) {
        this.workerRepository = workerRepository;
        this.businessTripRepository = businessTripRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) {
        if (workerRepository.count() > 0) {
            return; // se ci sono gia dati non li riinserisco
        }

        Worker mario = workerRepository.save(new Worker("mrossi", "Mario", "Rossi", "mario.rossi@mail.com"));
        Worker anna = workerRepository.save(new Worker("abianchi", "Anna", "Bianchi", "anna.bianchi@mail.com"));
        Worker luca = workerRepository.save(new Worker("lverdi", "Luca", "Verdi", "luca.verdi@mail.com"));
        Worker sara = workerRepository.save(new Worker("sferrari", "Sara", "Ferrari", "sara.ferrari@mail.com"));

        BusinessTrip milano = businessTripRepository.save(new BusinessTrip("Milano", LocalDate.of(2026, 8, 10)));
        BusinessTrip roma = businessTripRepository.save(new BusinessTrip("Roma", LocalDate.of(2026, 8, 20)));
        BusinessTrip londra = businessTripRepository.save(new BusinessTrip("Londra", LocalDate.of(2026, 9, 5)));

        BusinessTrip torino = new BusinessTrip("Torino", LocalDate.of(2026, 5, 1));
        torino.setStatus(TripState.COMPLETED);
        torino = businessTripRepository.save(torino);

        reservationRepository.save(new Reservation(LocalDate.of(2026, 7, 1), mario, milano, "finestrino, no scalo"));
        reservationRepository.save(new Reservation(LocalDate.of(2026, 7, 2), anna, roma, null));
        reservationRepository.save(new Reservation(LocalDate.of(2026, 7, 3), luca, londra, "hotel vicino stazione"));
        reservationRepository.save(new Reservation(LocalDate.of(2026, 7, 4), sara, torino, "auto a noleggio"));
        reservationRepository.save(new Reservation(LocalDate.of(2026, 7, 6), mario, londra, "corridoio"));
    }
}
