package net.project.TravelBuddy.service;

import net.project.TravelBuddy.payload.DayPlanDto;

import java.util.List;

public interface DayPlanService {
    DayPlanDto addDayPlan(Long tripId, DayPlanDto dayPlanDto);

    List<DayPlanDto> getDayPlansForTrip(Long tripId);

    DayPlanDto updateDayPlan(Long tripId, DayPlanDto dayPlanDto, Long dayPlanId);

    DayPlanDto getDayPlanById(Long tripId, Long dayPlanId);

    String deleteDayPlan(Long tripId, Long dayPlanId);
}
