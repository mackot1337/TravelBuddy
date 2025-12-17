package net.project.TravelBuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.project.TravelBuddy.entity.Accommodation;
import net.project.TravelBuddy.entity.Trip;
import net.project.TravelBuddy.exception.ApiException;
import net.project.TravelBuddy.exception.ResourceNotFoundException;
import net.project.TravelBuddy.external.dto.AmadeusHotelListResponse;
import net.project.TravelBuddy.external.dto.AmadeusHotelSearchResponse;
import net.project.TravelBuddy.payload.AccommodationDto;
import net.project.TravelBuddy.repository.AccommodationRepository;
import net.project.TravelBuddy.repository.TripRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccommodationService {

    @Qualifier("amadeusClient")
    private final WebClient amadeusClient;
    private final AmadeusAuthService authService;
    private final AccommodationRepository accommodationRepository;
    private final TripRepository tripRepository;
    private final ModelMapper modelMapper;

    public List<AccommodationDto> searchHotels(
            String cityCode,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        log.info("Searching hotels in: {} ({} to {})", cityCode, checkIn, checkOut);

        String token = authService.getAccessToken();

        List<AmadeusHotelListResponse.Hotel> hotels = getHotelsByCity(cityCode, token);

        if (hotels.isEmpty()) {
            throw new ApiException("No hotels found in city: " + cityCode);
        }

        log.info("Found {} hotels in {}", hotels.size(), cityCode);

        Map<String, AmadeusHotelListResponse.Hotel> hotelMap = hotels.stream()
                .collect(Collectors.toMap(
                        AmadeusHotelListResponse.Hotel::getHotelId,
                        h -> h
                ));

        String hotelIdsParam = hotels.stream()
                .limit(10)
                .map(AmadeusHotelListResponse.Hotel::getHotelId)
                .collect(Collectors.joining(","));

        try {
            AmadeusHotelSearchResponse response = amadeusClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("test.api.amadeus.com")
                            .path("/v3/shopping/hotel-offers")
                            .queryParam("hotelIds", hotelIdsParam)
                            .queryParam("checkInDate", checkIn.toString())
                            .queryParam("checkOutDate", checkOut.toString())
                            .queryParam("adults", 1)
                            .queryParam("currency", "EUR")
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(AmadeusHotelSearchResponse.class)
                    .block();

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                log.warn("No offers found for dates {} to {}", checkIn, checkOut);
                throw new ApiException("No hotel offers found for the given dates");
            }

            return response.getData().stream()
                    .map(hotelOffer -> mapToDto(hotelOffer, hotelMap, checkIn, checkOut, cityCode))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error calling Amadeus Hotel Offers API", e);
            throw new ApiException("Failed to search hotels: " + e.getMessage());
        }
    }

    private List<AmadeusHotelListResponse.Hotel> getHotelsByCity(String cityCode, String token) {
        try {
            AmadeusHotelListResponse response = amadeusClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("test.api.amadeus.com")
                            .path("/v1/reference-data/locations/hotels/by-city")
                            .queryParam("cityCode", cityCode)
                            .queryParam("radius", 20)
                            .queryParam("radiusUnit", "KM")
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(AmadeusHotelListResponse.class)
                    .block();

            if (response == null || response.getData() == null) {
                return List.of();
            }

            return response.getData().stream()
                    .limit(50)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error getting hotel list by city", e);
            throw new ApiException("Failed to get hotel list: " + e.getMessage());
        }
    }

    public AccommodationDto addToTrip(Long tripId, AccommodationDto dto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));

        Accommodation accommodation = modelMapper.map(dto, Accommodation.class);
        accommodation.setTrip(trip);

        Accommodation saved = accommodationRepository.save(accommodation);

        log.info("✅ Added accommodation {} to trip {}", saved.getName(), tripId);

        return modelMapper.map(saved, AccommodationDto.class);
    }

    public List<AccommodationDto> getByTripId(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip", "id", tripId);
        }

        return accommodationRepository.findByTripTripId(tripId).stream()
                .map(acc -> modelMapper.map(acc, AccommodationDto.class))
                .collect(Collectors.toList());
    }

    public void delete(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation", "id", accommodationId));

        accommodationRepository.delete(accommodation);
        log.info("✅ Deleted accommodation {}", accommodationId);
    }

    private AccommodationDto mapToDto(
            AmadeusHotelSearchResponse.HotelOffer hotelOffer,
            Map<String, AmadeusHotelListResponse.Hotel> hotelMap,
            LocalDate checkIn,
            LocalDate checkOut,
            String cityCode
    ) {
        AccommodationDto dto = new AccommodationDto();

        AmadeusHotelSearchResponse.HotelOffer.Hotel hotelFromOffer = hotelOffer.getHotel();
        dto.setHotelId(hotelFromOffer.getHotelId());
        dto.setName(hotelFromOffer.getName());

        AmadeusHotelListResponse.Hotel hotelDetails = hotelMap.get(hotelFromOffer.getHotelId());
        if (hotelDetails != null) {
            dto.setCityName(cityCode);

            if (hotelDetails.getGeoCode() != null) {
                dto.setLatitude(hotelDetails.getGeoCode().getLatitude());
                dto.setLongitude(hotelDetails.getGeoCode().getLongitude());
            }
        } else {
            dto.setLatitude(hotelFromOffer.getLatitude());
            dto.setLongitude(hotelFromOffer.getLongitude());
        }

        if (hotelFromOffer.getAddress() != null) {
            dto.setCityName(hotelFromOffer.getAddress().getCityName());
            dto.setCountryCode(hotelFromOffer.getAddress().getCountryCode());
            dto.setAddress(hotelFromOffer.getAddress().getLines());
        } else {
            dto.setCityName(cityCode);
            dto.setCountryCode(getCityCountryCode(cityCode));
        }

        dto.setCheckInDate(checkIn);
        dto.setCheckOutDate(checkOut);

        if (hotelOffer.getOffers() != null && !hotelOffer.getOffers().isEmpty()) {
            AmadeusHotelSearchResponse.HotelOffer.Offer offer = hotelOffer.getOffers().get(0);
            dto.setCurrency(offer.getPrice().getCurrency());
            dto.setPriceTotal(new BigDecimal(offer.getPrice().getTotal()));

            String searchUrl = String.format("https://www.booking.com/search?ss=%s&checkin=%s&checkout=%s",
                    dto.getCityName() != null ? dto.getCityName() : cityCode,
                    checkIn,
                    checkOut
            );
            dto.setBookingUrl(searchUrl);
        }

        return dto;
    }

    private String getCityCountryCode(String cityCode) {
        // Basic mapping (można rozszerzyć)
        Map<String, String> cityToCountry = Map.of(
                "PAR", "FR",
                "LON", "GB",
                "BCN", "ES",
                "MAD", "ES",
                "NYC", "US",
                "WAW", "PL",
                "BER", "DE",
                "ROM", "IT"
        );
        return cityToCountry.getOrDefault(cityCode, "");
    }
}