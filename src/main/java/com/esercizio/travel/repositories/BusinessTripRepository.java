package com.esercizio.travel.repositories;

import com.esercizio.travel.entities.BusinessTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessTripRepository extends JpaRepository<BusinessTrip, UUID> {
}
