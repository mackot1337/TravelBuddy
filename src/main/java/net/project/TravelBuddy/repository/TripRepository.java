package net.project.TravelBuddy.repository;

import net.project.TravelBuddy.entity.Trip;
import net.project.TravelBuddy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Long> {
    Trip findByName(String name);

    List<Trip> findAllByUser(User user);
}
