package com.esercizio.travel.entities;

import com.esercizio.travel.enums.TripState;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "business_trips")
@Getter
public class BusinessTrip {
    @Id
    @GeneratedValue
    private UUID id;
    private String destination;
    private LocalDate departureDate;
    @Enumerated(EnumType.STRING)
    private TripState status = TripState.PLANNED; // parte sempre planned

    public BusinessTrip() {
    }

    public BusinessTrip(String destination, LocalDate departureDate) {
        this.destination = destination;
        this.departureDate = departureDate;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public void setStatus(TripState status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BusinessTrip{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", departureDate=" + departureDate +
                ", status=" + status +
                '}';
    }
}
