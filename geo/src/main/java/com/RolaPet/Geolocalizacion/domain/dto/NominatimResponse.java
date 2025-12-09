package com.RolaPet.Geolocalizacion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominatimResponse {
    private String lat;
    private String lon;
    private String display_name;
    private String type;
    private String osm_type;
    private Long osm_id;
    private Address address;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String road;
        private String house_number;
        private String city;
        private String town;
        private String village;
        private String state;
        private String country;
        private String postcode;
        private String neighbourhood;
    }
}
