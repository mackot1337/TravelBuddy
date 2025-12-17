package net.project.TravelBuddy.controller;

import net.project.TravelBuddy.payload.TripDto;
import net.project.TravelBuddy.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<TripDto> createTrip(@RequestBody TripDto tripDto) {
        TripDto created = tripService.createTrip(tripDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TripDto>> getTrips() {
        List<TripDto> trips = tripService.getAllTripsForUser();
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripDto> getTrip(@PathVariable Long tripId) {
        TripDto trip = tripService.getTripById(tripId);
        return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<TripDto> updateTrip(@PathVariable Long tripId, @RequestBody TripDto tripDto) {
        TripDto trip = tripService.updateTrip(tripId, tripDto);
        return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<String> deleteTrip(@PathVariable Long tripId) {
        String status = tripService.deleteTrip(tripId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
