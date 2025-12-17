package net.project.TravelBuddy.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDto {
    private Long tripId;
    private String name;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    private List<DayPlanDto> dayPlans;
    private List<AccommodationDto> accommodations;
    private List<ExpenseDto> expenses;

    private BigDecimal totalExpenses;
}
