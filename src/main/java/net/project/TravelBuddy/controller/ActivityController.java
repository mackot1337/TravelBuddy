package net.project.TravelBuddy.controller;

import net.project.TravelBuddy.payload.ActivityDto;
import net.project.TravelBuddy.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips/{tripId}/dayplans/{dayPlanId}/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityDto> createActivity(@RequestBody ActivityDto activityDto, @PathVariable Long tripId, @PathVariable Long dayPlanId) {
        ActivityDto createdActivity = activityService.addActivity(activityDto, tripId, dayPlanId);
        return new ResponseEntity<>(createdActivity, HttpStatus.CREATED);
    }

}
