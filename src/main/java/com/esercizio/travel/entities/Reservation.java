package com.esercizio.travel.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
public class Reservation {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "booking_date")
    private LocalDate bookingDate;
    // relazioni unidirezionali, Worker e Trip non sanno delle prenotazioni
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;
    @ManyToOne
    @JoinColumn(name = "business_trip_id", nullable = false)
    private BusinessTrip businessTrip;
    private String preferences; // tipo finestrino, hotel vicino ecc, puo' essere vuoto

    public Reservation() {
    }

    public Reservation(LocalDate bookingDate, Worker worker, BusinessTrip businessTrip, String preferences) {
        this.bookingDate = bookingDate;
        this.worker = worker;
        this.businessTrip = businessTrip;
        this.preferences = preferences;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setBusinessTrip(BusinessTrip businessTrip) {
        this.businessTrip = businessTrip;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", bookingDate=" + bookingDate +
                ", worker=" + worker +
                ", businessTrip=" + businessTrip +
                ", preferences='" + preferences + '\'' +
                '}';
    }
}
