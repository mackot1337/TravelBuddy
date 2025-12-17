package net.project.TravelBuddy.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.project.TravelBuddy.entity.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private Long expenseId;
    private Long tripId;
    private BigDecimal amount;
    private String currency;
    private String description;
    private ExpenseCategory category;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate expenseDate;
}
