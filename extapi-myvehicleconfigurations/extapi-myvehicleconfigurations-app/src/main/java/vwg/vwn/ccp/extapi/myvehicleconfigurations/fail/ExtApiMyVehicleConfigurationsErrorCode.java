/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.fail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vwg.vwn.ccp.http.fail.ApiErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExtApiMyVehicleConfigurationsErrorCode implements ApiErrorCode {

    // @formatter:off
    INVALID_PARAMETER   (400, "BCC400", "The entered parameter is invalid."                                 ),
    NO_DATA_FOUND       (404, "BCC401", "No data found for requested carId."                                ),
    UNKNOWN_SERVER_ERROR(500, "BCC500", "The data integration service responded with an unknown error type."),
    DIS_NOT_AVAILABLE   (502, "BCC501", "The data integration service is not available."                    );
     // @formatter:on

    private int status;
    private String errorCode;
    private String description;
}
