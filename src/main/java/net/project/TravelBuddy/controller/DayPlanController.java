package net.project.TravelBuddy.controller;

import net.project.TravelBuddy.entity.DayPlan;
import net.project.TravelBuddy.exception.ApiException;
import net.project.TravelBuddy.payload.DayPlanDto;
import net.project.TravelBuddy.service.DayPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/dayplans")
public class DayPlanController {

    @Autowired
    private DayPlanService dayPlanService;

    @PostMapping
    public ResponseEntity<DayPlanDto> addDayPlan(@PathVariable Long tripId, @RequestBody DayPlanDto dayPlanDto){
        DayPlanDto dayPlan = dayPlanService.addDayPlan(tripId, dayPlanDto);
        return new ResponseEntity<>(dayPlan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DayPlanDto>> getDayPlansForTrip(@PathVariable Long tripId){
        List<DayPlanDto> dayPlans = dayPlanService.getDayPlansForTrip(tripId);
        return new ResponseEntity<>(dayPlans, HttpStatus.OK);
    }

    @GetMapping("/{dayPlanId}")
    public ResponseEntity<DayPlanDto> getDayPlanById(@PathVariable Long tripId, @PathVariable Long dayPlanId){
        DayPlanDto dayPlan = dayPlanService.getDayPlanById(tripId, dayPlanId);
        return new ResponseEntity<>(dayPlan, HttpStatus.OK);
    }

    @PutMapping("/{dayPlanId}")
    public ResponseEntity<DayPlanDto> updateDayPlan(@PathVariable Long tripId, @PathVariable Long dayPlanId, @RequestBody DayPlanDto dayPlanDto){
        DayPlanDto updatedDayPlan = dayPlanService.updateDayPlan(tripId, dayPlanDto, dayPlanId);
        return new ResponseEntity<>(updatedDayPlan, HttpStatus.OK);
    }

    @DeleteMapping("/{dayPlanId}")
    public ResponseEntity<String> deleteDayPlan(@PathVariable Long tripId, @PathVariable Long dayPlanId) {
        String status = dayPlanService.deleteDayPlan(tripId, dayPlanId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
