package net.project.TravelBuddy.service.Impl;

import net.project.TravelBuddy.entity.DayPlan;
import net.project.TravelBuddy.entity.Trip;
import net.project.TravelBuddy.exception.ApiException;
import net.project.TravelBuddy.payload.DayPlanDto;
import net.project.TravelBuddy.repository.DayPlanRepository;
import net.project.TravelBuddy.repository.TripRepository;
import net.project.TravelBuddy.service.DayPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DayPlanServiceImpl implements DayPlanService {

    @Autowired
    private DayPlanRepository dayPlanRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TripRepository tripRepository;

    @Override
    public DayPlanDto addDayPlan(Long tripId, DayPlanDto dayPlanDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to add a day plan to this trip");
        }
        DayPlan newDayPlan = new DayPlan();
        newDayPlan.setTrip(trip);
        newDayPlan.setDate(dayPlanDto.getDate());
        DayPlan savedDayPlan = dayPlanRepository.save(newDayPlan);
        return modelMapper.map(savedDayPlan, DayPlanDto.class);
    }

    @Override
    public List<DayPlanDto> getDayPlansForTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to view day plans for this trip");
        }
        List<DayPlan> dayPlans = dayPlanRepository.findAllByTrip(trip);
        if (dayPlans.isEmpty()) {
            throw new ApiException("No day plans found for this trip");
        }
        return dayPlans.stream()
                .map(dayPlan -> modelMapper.map(dayPlan, DayPlanDto.class))
                .toList();
    }

    @Override
    public DayPlanDto updateDayPlan(Long tripId, DayPlanDto dayPlanDto, Long dayPlanId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to update a day plan for this trip");
        }
        Optional<DayPlan> optionalDayPlan = dayPlanRepository.findById(dayPlanId);
        if (optionalDayPlan.isEmpty()) {
            throw new ApiException("Day plan not found");
        }
        DayPlan existingDayPlan = optionalDayPlan.get();
        if (!existingDayPlan.getTrip().getTripId().equals(trip.getTripId())) {
            throw new ApiException("Day plan does not belong to the specified trip");
        }
        existingDayPlan.setDate(dayPlanDto.getDate());
        DayPlan updatedDayPlan = dayPlanRepository.save(existingDayPlan);
        return modelMapper.map(updatedDayPlan, DayPlanDto.class);
    }

    @Override
    public DayPlanDto getDayPlanById(Long tripId, Long dayPlanId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to view a day plan for this trip");
        }
        Optional<DayPlan> optionalDayPlan = dayPlanRepository.findById(dayPlanId);
        if (optionalDayPlan.isEmpty()) {
            throw new ApiException("Day plan not found");
        }
        DayPlan dayPlan = optionalDayPlan.get();
        if (!dayPlan.getTrip().getTripId().equals(trip.getTripId())) {
            throw new ApiException("Day plan does not belong to the specified trip");
        }
        return modelMapper.map(dayPlan, DayPlanDto.class);
    }

    @Override
    public String deleteDayPlan(Long tripId, Long dayPlanId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to delete a day plan for this trip");
        }
        Optional<DayPlan> optionalDayPlan = dayPlanRepository.findById(dayPlanId);
        if (optionalDayPlan.isEmpty()) {
            throw new ApiException("Day plan not found");
        }
        DayPlan dayPlan = optionalDayPlan.get();
        if (!dayPlan.getTrip().getTripId().equals(trip.getTripId())) {
            throw new ApiException("Day plan does not belong to the specified trip");
        }
        dayPlanRepository.delete(dayPlan);
        return "Day plan deleted successfully";
    }
}
