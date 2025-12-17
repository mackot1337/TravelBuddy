package net.project.TravelBuddy.service.Impl;

import net.project.TravelBuddy.entity.Activity;
import net.project.TravelBuddy.entity.DayPlan;
import net.project.TravelBuddy.entity.Trip;
import net.project.TravelBuddy.exception.ApiException;
import net.project.TravelBuddy.exception.ResourceNotFoundException;
import net.project.TravelBuddy.payload.ActivityDto;
import net.project.TravelBuddy.repository.ActivityRepository;
import net.project.TravelBuddy.repository.DayPlanRepository;
import net.project.TravelBuddy.repository.TripRepository;
import net.project.TravelBuddy.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private DayPlanRepository dayPlanRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public ActivityDto addActivity(ActivityDto activityDto, Long tripId, Long dayPlanId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to add an activity to this trip");
        }
        DayPlan dayPlan = dayPlanRepository.findById(dayPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Day plan", "id", dayPlanId));
        if (!dayPlan.getTrip().getTripId().equals(tripId)) {
            throw new ApiException("Day plan does not belong to the specified trip");
        }

        Activity activity = new Activity();
        activity.setTitle(activityDto.getTitle());
        activity.setNote(activityDto.getNote());
        activity.setStartTime(activityDto.getStartTime());
        activity.setEndTime(activityDto.getEndTime());
        activity.setDayPlan(dayPlan);

        Activity savedActivity = activityRepository.save(activity);

        ActivityDto out = new ActivityDto();
        out.setActivityId(savedActivity.getActivityId());
        out.setDayPlanId(dayPlanId);
        out.setTitle(savedActivity.getTitle());
        out.setNote(savedActivity.getNote());
        out.setStartTime(savedActivity.getStartTime());
        out.setEndTime(savedActivity.getEndTime());

        return out;
    }
}
