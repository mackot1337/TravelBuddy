package net.project.TravelBuddy.service;

import net.project.TravelBuddy.payload.ActivityDto;

public interface ActivityService {
    ActivityDto addActivity(ActivityDto activityDto, Long tripId, Long dayPlanId);
}
