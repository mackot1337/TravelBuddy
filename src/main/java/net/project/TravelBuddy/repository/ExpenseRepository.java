package net.project.TravelBuddy.repository;

import net.project.TravelBuddy.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("select coalesce(sum(e.amount), 0) from Expense e where e.trip.tripId = :tripId")
    BigDecimal sumByTripId(@Param("tripId") Long tripId);
}
