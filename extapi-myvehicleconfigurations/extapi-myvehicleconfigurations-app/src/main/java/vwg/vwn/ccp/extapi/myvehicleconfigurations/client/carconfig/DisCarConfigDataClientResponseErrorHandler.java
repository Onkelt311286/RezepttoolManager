/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.client.carconfig;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.fail.ExtApiException;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.fail.ExtApiMyVehicleConfigurationsErrorCode;
import vwg.vwn.ccp.http.fail.ApiError;
import vwg.vwn.ccp.http.fail.ApiException;

@Slf4j
@Component
public class DisCarConfigDataClientResponseErrorHandler implements ResponseErrorHandler {

    private static final String RESPONSE_BODY_MISSING_MESSAGE =
            "The body of the response is null, but it must contain an error JSON of type ApiError.";

    private final ObjectMapper objectMapper;

    public DisCarConfigDataClientResponseErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.trace("Start handling client error response.");

        final var statusCode = response.getStatusCode();

        log.info("DIS responded with status code '{}'.", statusCode);

        if (statusCode == HttpStatus.BAD_GATEWAY) {
            log.info("The requested DIS is not available.");

            throw new ApiException(ExtApiMyVehicleConfigurationsErrorCode.DIS_NOT_AVAILABLE);
        }
        else if(statusCode == HttpStatus.NOT_FOUND) {
            
        }

        final var responseBody = requireNonNull(response.getBody(), RESPONSE_BODY_MISSING_MESSAGE);
        final var apiError = extractApiErrorFromBody(responseBody);

        throw new ExtApiException(apiError);
    }

    private ApiError extractApiErrorFromBody(InputStream body) {
        try {
            return objectMapper.readValue(body.readAllBytes(), ApiError.class);
        } catch (final IOException ex) {
            log.error("Unable to parse the ApiError object from DIS.");
            throw new ApiException(ExtApiMyVehicleConfigurationsErrorCode.UNKNOWN_SERVER_ERROR, ex.getMessage());
        }
    }
}
