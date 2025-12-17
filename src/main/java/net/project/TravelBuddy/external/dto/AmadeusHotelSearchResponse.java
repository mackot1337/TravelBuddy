package net.project.TravelBuddy.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class AmadeusHotelSearchResponse {
    private List<HotelOffer> data;

    @Data
    public static class HotelOffer {
        private String type;
        private Hotel hotel;
        private List<Offer> offers;

        @Data
        public static class Hotel {
            private String hotelId;
            private String name;

            @JsonProperty("cityCode")
            private String cityCode;

            private Double latitude;
            private Double longitude;

            private Address address;

            @Data
            public static class Address {
                private String countryCode;
                private String cityName;
                private String lines;
            }
        }

        @Data
        public static class Offer {
            private String id;

            @JsonProperty("checkInDate")
            private String checkInDate;

            @JsonProperty("checkOutDate")
            private String checkOutDate;

            private Price price;

            @Data
            public static class Price {
                private String currency;
                private String total;
            }
        }
    }
}