package net.project.TravelBuddy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Trip name is mandatory")
    @Size(min = 1, max = 100, message = "Trip name must be between 1 and 100 characters")
    private String name;
    @NotNull(message = "Start date is mandatory")
    @Column(nullable = false)
    private LocalDate startDate;
    @NotNull(message = "End date is mandatory")
    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "trip", cascade =   CascadeType.ALL, orphanRemoval = true)
    @OrderBy("checkInDate ASC")
    private List<Accommodation> accommodations = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade =  CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date ASC")
    private List<DayPlan> dayPlans = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @AssertTrue(message = "End date must be after start date")
    private boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate) || endDate.isEqual(startDate);
    }

    @Transient
    public BigDecimal getTotalExpenses() {
        return expenses.stream()
                .map(e -> e.getAmount())
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }
}
