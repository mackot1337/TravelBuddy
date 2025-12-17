package net.project.TravelBuddy.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayPlanDto {
    private Long dayPlanId;
    private Long tripId;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;
    private List<ActivityDto> activities;
}
