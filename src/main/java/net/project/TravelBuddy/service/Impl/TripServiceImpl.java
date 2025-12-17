package net.project.TravelBuddy.service.Impl;

import net.project.TravelBuddy.entity.Trip;
import net.project.TravelBuddy.entity.User;
import net.project.TravelBuddy.exception.ApiException;
import net.project.TravelBuddy.mapper.TripMapper;
import net.project.TravelBuddy.payload.TripDto;
import net.project.TravelBuddy.repository.ExpenseRepository;
import net.project.TravelBuddy.repository.TripRepository;
import net.project.TravelBuddy.repository.UserRepository;
import net.project.TravelBuddy.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public TripDto createTrip(TripDto tripDto) {
        Trip existing = tripRepository.findByName(tripDto.getName());
        if (existing != null) {
            throw new ApiException("Trip with name " + tripDto.getName() + " already exists.");
        }

        Trip trip = new Trip();
        trip.setName(tripDto.getName());
        trip.setStartDate(tripDto.getStartDate());
        trip.setEndDate(tripDto.getEndDate());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException("User not found"));
        trip.setUser(user);

        Trip savedTrip = tripRepository.save(trip);
        return TripMapper.toDto(savedTrip);
    }

    @Override
    public List<TripDto> getAllTripsForUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ApiException("User not found"));

        return tripRepository.findAllByUser(user)
                .stream()
                .map(TripMapper::toDto)
                .toList();
    }

    @Override
    public TripDto getTripById(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to access this trip.");
        }

        TripDto tripDto = TripMapper.toDto(trip);
        tripDto.setTotalExpenses(expenseRepository.sumByTripId(tripId));
        return tripDto;
    }

    @Override
    public TripDto updateTrip(Long tripId, TripDto tripDto) {
        Trip existing = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existing.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to update this trip.");
        }

        existing.setName(tripDto.getName());
        existing.setStartDate(tripDto.getStartDate());
        existing.setEndDate(tripDto.getEndDate());

        Trip updatedTrip = tripRepository.save(existing);
        return TripMapper.toDto(updatedTrip);
    }

    @Override
    public String deleteTrip(Long tripId) {
        Trip existing = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException("Trip not found with id: " + tripId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existing.getUser().getUsername().equals(username)) {
            throw new ApiException("You do not have permission to delete this trip.");
        }

        tripRepository.delete(existing);
        return "Trip deleted successfully.";
    }
}
