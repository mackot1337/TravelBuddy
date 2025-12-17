
package net.project.TravelBuddy.mapper;

import net.project.TravelBuddy.entity.*;
import net.project.TravelBuddy.payload.*;

import java.util.stream.Collectors;
import java.util.List;

public final class TripMapper {
    private TripMapper(){}

    public static TripDto toDto(Trip t){
        if (t == null) return null;
        TripDto dto = new TripDto();
        dto.setTripId(t.getTripId());
        dto.setName(t.getName());
        dto.setStartDate(t.getStartDate());
        dto.setEndDate(t.getEndDate());
        dto.setTotalExpenses(t.getTotalExpenses());

        if (t.getDayPlans() != null){
            List<DayPlanDto> plans = t.getDayPlans().stream().map(TripMapper::toDto).collect(Collectors.toList());
            dto.setDayPlans(plans);
        }

        if (t.getAccommodations() != null){
            List<AccommodationDto> acc = t.getAccommodations().stream().map(TripMapper::toDto).collect(Collectors.toList());
            dto.setAccommodations(acc);
        }

        if (t.getExpenses() != null){
            List<ExpenseDto> ex = t.getExpenses().stream().map(TripMapper::toDto).collect(Collectors.toList());
            dto.setExpenses(ex);
        }

        return dto;
    }

    public static DayPlanDto toDto(DayPlan dp){
        if (dp == null) return null;
        DayPlanDto dto = new DayPlanDto();
        dto.setDayPlanId(dp.getDayPlanId());
        dto.setTripId(dp.getTrip() != null ? dp.getTrip().getTripId() : null);
        dto.setDate(dp.getDate());
        if (dp.getActivities() != null){
            List<ActivityDto> acts = dp.getActivities().stream().map(TripMapper::toDto).collect(Collectors.toList());
            dto.setActivities(acts);
        }
        return dto;
    }

    public static ActivityDto toDto(Activity a){
        if (a == null) return null;
        ActivityDto dto = new ActivityDto();
        dto.setActivityId(a.getActivityId());
        dto.setDayPlanId(a.getDayPlan() != null ? a.getDayPlan().getDayPlanId() : null);
        dto.setTitle(a.getTitle());
        dto.setNote(a.getNote());
        dto.setStartTime(a.getStartTime());
        dto.setEndTime(a.getEndTime());
        return dto;
    }

    public static AccommodationDto toDto(Accommodation a){
        if (a == null) return null;
        AccommodationDto dto = new AccommodationDto();
        dto.setAccommodationId(a.getAccommodationId());
        dto.setTripId(a.getTrip() != null ? a.getTrip().getTripId() : null);
        dto.setCityName(a.getCityName());
        dto.setCountryCode(a.getCountryCode());
        dto.setName(a.getName());
        dto.setAddress(a.getAddress());
        dto.setCheckInDate(a.getCheckInDate());
        dto.setCheckOutDate(a.getCheckOutDate());
        dto.setLatitude(a.getLatitude());
        dto.setLongitude(a.getLongitude());
        dto.setCurrency(a.getCurrency());
        dto.setPriceTotal(a.getPriceTotal());
        dto.setBookingUrl(a.getBookingUrl());
        return dto;
    }

    public static ExpenseDto toDto(Expense e){
        if (e == null) return null;
        ExpenseDto dto = new ExpenseDto();
        dto.setExpenseId(e.getExpenseId());
        dto.setTripId(e.getTrip() != null ? e.getTrip().getTripId() : null);
        dto.setAmount(e.getAmount());
        dto.setCurrency(e.getCurrency());
        dto.setDescription(e.getDescription());
        dto.setCategory(e.getCategory());
        dto.setExpenseDate(e.getExpenseDate());
        return dto;
    }
}
