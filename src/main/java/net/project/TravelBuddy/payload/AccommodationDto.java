package net.project.TravelBuddy.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDto {
    private Long accommodationId;
    private String hotelId;
    private Long tripId;
    private String cityName;
    private String countryCode;
    private String name;
    private String address;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double latitude;
    private Double longitude;
    private String currency;
    private BigDecimal priceTotal;
    private String bookingUrl;
}
