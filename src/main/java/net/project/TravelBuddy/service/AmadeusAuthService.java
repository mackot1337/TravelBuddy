package net.project.TravelBuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.project.TravelBuddy.exception.ApiException;
import net.project.TravelBuddy.external.dto.AmadeusTokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmadeusAuthService {

    @Qualifier("amadeusClient")
    private final WebClient amadeusClient;

    @Value("${AMADEUS_API_KEY}")
    private String apiKey;

    @Value("${AMADEUS_API_SECRET}")
    private String apiSecret;

    private String accessToken;
    private Instant tokenExpiry;

    public String getAccessToken() {
        if (accessToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
            return accessToken;
        }

        return fetchNewToken();
    }

    private String fetchNewToken() {
        log.info("Fetching new Amadeus access token");

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "client_credentials");
            formData.add("client_id", apiKey);
            formData.add("client_secret", apiSecret);

            AmadeusTokenResponse response = amadeusClient.post()
                    .uri("https://test.api.amadeus.com/v1/security/oauth2/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(AmadeusTokenResponse.class)
                    .block();

            if (response == null || response.getAccessToken() == null) {
                throw new ApiException("Failed to get Amadeus access token");
            }

            this.accessToken = response.getAccessToken();
            this.tokenExpiry = Instant.now().plusSeconds(response.getExpiresIn() - 60);

            log.info("✅ Amadeus token obtained, expires in {} seconds", response.getExpiresIn());

            return accessToken;

        } catch (Exception e) {
            log.error("Error fetching Amadeus token", e);
            throw new ApiException("Failed to authenticate with Amadeus API: " + e.getMessage());
        }
    }
}