package com.RolaPet.Geolocalizacion.client;
import org.springframework.http.HttpHeaders;
import com.RolaPet.Geolocalizacion.domain.dto.NominatimResponse;
import com.RolaPet.Geolocalizacion.exception.GeocodeNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.common.util.concurrent.RateLimiter;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
@Slf4j
public class NominatimClient {

    private final RestTemplate restTemplate;
    private final RateLimiter rateLimiter;

    @Value("${geolocalizacion.nominatim.base-url:https://nominatim.openstreetmap.org}")
    private String baseUrl;

    @Value("${geolocalizacion.nominatim.user-agent:ROLA-PET-Platform/1.0}")
    private String userAgent;

    public NominatimClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.rateLimiter = RateLimiter.create(1.0);
    }

    public NominatimResponse search(String query) {
        log.info("Buscando dirección en Nominatim: {}", query);
        rateLimiter.acquire();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", userAgent);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/search")
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .queryParam("limit", "1");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            URI uri = builder.build().encode().toUri();   // ✅ Codifica correctamente
            log.info("URL generada: {}", uri);

            ResponseEntity<NominatimResponse[]> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    NominatimResponse[].class
            );

            if (response.getBody() != null && response.getBody().length > 0) {
                return response.getBody()[0];
            }
            throw new GeocodeNotFoundException("No se encontró la dirección: " + query);
        } catch (Exception e) {
            log.error("Error en geocoding: {}", e.getMessage());
            throw new GeocodeNotFoundException("Error en geocoding: " + e.getMessage());
        }
    }



    public NominatimResponse reverse(double lat, double lon) {
        log.info("Reverse geocoding: ({}, {})", lat, lon);

        rateLimiter.acquire();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", userAgent);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/reverse")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .build(true)  // ❗ EVITA DOBLE ENCODE
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<NominatimResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    NominatimResponse.class
            );

            NominatimResponse result = response.getBody();
            if (result != null) {
                log.info("Dirección encontrada: {}", result.getDisplay_name());
                return result;
            }

            throw new GeocodeNotFoundException("Error");

        } catch (Exception e) {
            log.error("Error en reverse geocoding: {}", e.getMessage());
            throw new GeocodeNotFoundException("Error en reverse geocoding: " + e.getMessage());
        }
    }
}

