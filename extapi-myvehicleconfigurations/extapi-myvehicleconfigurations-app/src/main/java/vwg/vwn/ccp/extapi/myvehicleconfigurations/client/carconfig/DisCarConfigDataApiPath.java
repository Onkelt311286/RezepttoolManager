/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.client.carconfig;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DisCarConfigDataApiPath {

    VEHICLE_CONFIGURATIONS("/myvehicleconfiguration"),
    VEHICLE_CONFIGURATIONS_CONFIGURE("/myvehicleconfiguration/configure");

    private String path;
}
