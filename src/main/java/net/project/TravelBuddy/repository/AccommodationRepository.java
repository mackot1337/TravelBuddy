package net.project.TravelBuddy.repository;

import net.project.TravelBuddy.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByTripTripId(Long tripId);
}
