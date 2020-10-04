/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import vwg.vwn.ccp.dis.carconfig.model.AllVehicleConfigurationsForStorage;
import vwg.vwn.ccp.dis.carconfig.model.VehicleConfigurationForStorage;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.api.MyVehicleConfigurationsApiDelegate;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.client.carconfig.DisCarConfigDataClient;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.fail.ExtApiMyVehicleConfigurationsErrorCode;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.AllMyVehicleConfigurations;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.MyVehicleConfiguration;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.service.mapper.VehicleConfigurationMapper;
import vwg.vwn.ccp.http.fail.ApiException;

@Service
@AllArgsConstructor
public class MyVehicleConfigurationsService implements MyVehicleConfigurationsApiDelegate {

    private DisCarConfigDataClient carConfigClient;

    @Override
    public ResponseEntity<AllMyVehicleConfigurations> fetchAllMyConfigurations() {
        final ResponseEntity<AllVehicleConfigurationsForStorage> disResponse =
                carConfigClient.fetchAllMyVehicleConfigurations();
        if (NOT_FOUND == disResponse.getStatusCode() || Objects.isNull(disResponse.getBody().getVehicleConfigurations())
                || disResponse.getBody().getVehicleConfigurations().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(VehicleConfigurationMapper.mapDisConfigToBffConfig(disResponse.getBody()));
        }
    }

    @Override
    public ResponseEntity<MyVehicleConfiguration> fetchMyConfiguration(String carId) {
        final ResponseEntity<VehicleConfigurationForStorage> disResponse =
                carConfigClient.fetchMyVehicleConfiguration(carId);
        if (NOT_FOUND == disResponse.getStatusCode()) {
            throw new ApiException(ExtApiMyVehicleConfigurationsErrorCode.NO_DATA_FOUND);
        } else {
            return ResponseEntity.ok(VehicleConfigurationMapper.mapDisConfigToBffConfig(disResponse.getBody()));
        }
    }

    @Override
    public ResponseEntity<Void> storeOrUpdateConfiguration(MyVehicleConfiguration myVehicleConfiguration) {
        return carConfigClient.addOrUpdateVehicleConfiguration(
                VehicleConfigurationMapper.mapBffConfigToDisConfig(myVehicleConfiguration));
    }

    @Override
    public ResponseEntity<Void> deleteMyConfiguration(String carId) {
        ResponseEntity<Void> disResponse = carConfigClient.deleteVehicleConfiguration(carId);
        if (NOT_FOUND == disResponse.getStatusCode()) {
            throw new ApiException(ExtApiMyVehicleConfigurationsErrorCode.NO_DATA_FOUND);
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
