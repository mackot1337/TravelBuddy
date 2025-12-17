package net.project.TravelBuddy.repository;

import net.project.TravelBuddy.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
