package net.project.TravelBuddy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.project.TravelBuddy.payload.AccommodationDto;
import net.project.TravelBuddy.service.AccommodationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/search")
    public ResponseEntity<List<AccommodationDto>> searchHotels(
            @RequestParam String cityCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {
        List<AccommodationDto> results = accommodationService.searchHotels(cityCode, checkIn, checkOut);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/trips/{tripId}")
    public ResponseEntity<AccommodationDto> addToTrip(
            @PathVariable Long tripId,
            @Valid @RequestBody AccommodationDto dto
    ) {
        AccommodationDto saved = accommodationService.addToTrip(tripId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accommodationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}