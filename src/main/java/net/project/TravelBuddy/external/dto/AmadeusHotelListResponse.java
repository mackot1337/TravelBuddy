package net.project.TravelBuddy.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class AmadeusHotelListResponse {
    private List<Hotel> data;

    @Data
    public static class Hotel {
        @JsonProperty("hotelId")
        private String hotelId;

        private String name;

        @JsonProperty("iataCode")
        private String iataCode;

        private GeoCode geoCode;

        @Data
        public static class GeoCode {
            private Double latitude;
            private Double longitude;
        }
    }
}