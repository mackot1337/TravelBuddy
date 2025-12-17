package net.project.TravelBuddy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accommodations")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accommodationId;

    @Column(length = 100)
    private String hotelId;

    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @NotBlank(message = "City name is mandatory")
    @Column(length = 100, nullable = false)
    private String cityName;
    @Size(min = 2, max = 3, message = "Country code must be 2 or 3 characters")
    @Column(length = 3)
    private String countryCode;

    @NotBlank(message = "Hotel name is mandatory")
    @Column(length = 300, nullable = false)
    private String name;
    private String address;
    @Column(nullable = false)
    @NotNull(message = "Check in date is mandatory")
    private LocalDate checkInDate;
    @Column(nullable = false)
    @NotNull(message = "Check out date is mandatory")
    private LocalDate checkOutDate;

    private Double latitude;
    private Double longitude;

    @NotBlank(message = "Currency is mandatory")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    @Column(nullable = false)
    private String currency;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal priceTotal;

    @NotBlank(message = "Booking URL is mandatory")
    private String bookingUrl;

    @AssertTrue(message = "Check-out date must be after check-in date")
    private boolean isCheckOutDateAfterCheckInDate() {
        if (checkInDate == null || checkOutDate == null) {
            return true;
        }
        return checkOutDate.isAfter(checkInDate);
    }

}
