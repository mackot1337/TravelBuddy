package net.project.TravelBuddy.repository;

import net.project.TravelBuddy.entity.DayPlan;
import net.project.TravelBuddy.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayPlanRepository extends JpaRepository<DayPlan, Long> {
    List<DayPlan> findAllByTrip(Trip trip);
}
