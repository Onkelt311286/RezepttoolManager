/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.client.carconfig;

import org.springframework.http.ResponseEntity;
import vwg.vwn.ccp.dis.carconfig.model.AllVehicleConfigurationsForStorage;
import vwg.vwn.ccp.dis.carconfig.model.VehicleConfigurationForStorage;

public interface DisCarConfigDataClient {

    ResponseEntity<AllVehicleConfigurationsForStorage> fetchAllMyVehicleConfigurations();

    ResponseEntity<VehicleConfigurationForStorage> fetchMyVehicleConfiguration(String carId);

    ResponseEntity<Void> addOrUpdateVehicleConfiguration(VehicleConfigurationForStorage configuration);

    ResponseEntity<Void> deleteVehicleConfiguration(String carId);
}
