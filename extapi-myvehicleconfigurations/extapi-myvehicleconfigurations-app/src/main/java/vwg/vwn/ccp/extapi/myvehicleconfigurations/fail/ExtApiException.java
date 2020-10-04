/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.fail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vwg.vwn.ccp.http.fail.ApiError;

@Getter
@AllArgsConstructor
public class ExtApiException extends RuntimeException {

    private static final long serialVersionUID = 2610548858388505069L;

    private final ApiError apiError;
}
