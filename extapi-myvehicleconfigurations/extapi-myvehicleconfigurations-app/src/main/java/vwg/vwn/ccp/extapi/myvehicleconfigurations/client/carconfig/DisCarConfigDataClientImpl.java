/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.client.carconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;
import vwg.vwn.ccp.dis.carconfig.model.AllVehicleConfigurationsForStorage;
import vwg.vwn.ccp.dis.carconfig.model.VehicleConfigurationForStorage;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.fail.ExtApiException;

@Slf4j
@Component
public class DisCarConfigDataClientImpl implements DisCarConfigDataClient {

    private final RestTemplate restTemplate;

    public DisCarConfigDataClientImpl(@Qualifier("disCarconfigDataRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public ResponseEntity<AllVehicleConfigurationsForStorage> fetchAllMyVehicleConfigurations() {
        try {
            return restTemplate.getForEntity(DisCarConfigDataApiPath.VEHICLE_CONFIGURATIONS.getPath(),
                    AllVehicleConfigurationsForStorage.class);
        } catch (ExtApiException e) {
            if (e.getApiError().getStatus().equals(404)) {
                return ResponseEntity.notFound().build();
            } else {
                throw e;
            }
        }
    }

    @Override
    public ResponseEntity<Void> addOrUpdateVehicleConfiguration(VehicleConfigurationForStorage configuration) {
        HttpEntity<VehicleConfigurationForStorage> requestEntity = new HttpEntity<>(configuration, createHeaders());
        restTemplate.postForEntity(DisCarConfigDataApiPath.VEHICLE_CONFIGURATIONS_CONFIGURE.getPath(), requestEntity,
                VehicleConfigurationForStorage.class);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<VehicleConfigurationForStorage> fetchMyVehicleConfiguration(String carId) {
        // @formatter:off
        String url = UriComponentsBuilder
                .fromPath(DisCarConfigDataApiPath.VEHICLE_CONFIGURATIONS.getPath() + "/{carId}")
                .buildAndExpand(carId)
                .toUriString();
        // @formatter:on
        try {
            return restTemplate.getForEntity(url, VehicleConfigurationForStorage.class);
        } catch (ExtApiException e) {
            if (e.getApiError().getStatus().equals(404)) {
                return ResponseEntity.notFound().build();
            } else {
                throw e;
            }
        }
    }

    @Override
    public ResponseEntity<Void> deleteVehicleConfiguration(String carId) {
        // @formatter:off
        String url = UriComponentsBuilder
                .fromPath(DisCarConfigDataApiPath.VEHICLE_CONFIGURATIONS.getPath() + "/{carId}")
                .buildAndExpand(carId)
                .toUriString();
        // @formatter:on
        try {
            restTemplate.delete(url);
            return ResponseEntity.ok().build();
        } catch (ExtApiException e) {
            if (e.getApiError().getStatus().equals(404)) {
                return ResponseEntity.notFound().build();
            } else {
                throw e;
            }
        }
    }
}
