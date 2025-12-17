package net.project.TravelBuddy.service;

import net.project.TravelBuddy.entity.Trip;
import net.project.TravelBuddy.payload.TripDto;

import java.util.List;

public interface TripService {
    TripDto createTrip(TripDto tripDto);

    List<TripDto> getAllTripsForUser();

    TripDto getTripById(Long tripId);

    TripDto updateTrip(Long tripId, TripDto tripDto);

    String deleteTrip(Long tripId);
}
