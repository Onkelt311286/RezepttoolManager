/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.fail;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vwg.vwn.ccp.http.fail.AbstractApiErrorHandler;
import vwg.vwn.ccp.http.fail.ApiError;
import vwg.vwn.ccp.http.fail.ApiException;

@ControllerAdvice
public class ExtApiMyVehicleConfigurationsErrorHandler extends AbstractApiErrorHandler {

    @ExceptionHandler(ExtApiException.class)
    public ResponseEntity<ApiError> handleBffApiException(ExtApiException ex, WebRequest request) {
        final var apiError = ex.getApiError().path(request.getDescription(false));

        logger.error(apiError, ex);

        return ResponseEntity.status(apiError.getStatus()).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(apiError);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex, WebRequest request) {
        final var apiError = createApiError(ex.getErrorCode(), request.getDescription(false), ex.getMessage());

        logger.error(apiError, ex);

        return ResponseEntity.status(apiError.getStatus()).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(apiError);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException ex, WebRequest request) {
        final var apiError = createApiError(ExtApiMyVehicleConfigurationsErrorCode.UNKNOWN_SERVER_ERROR, request.getDescription(false),
                ex.getMessage());

        logger.error(apiError, ex);

        return ResponseEntity.status(apiError.getStatus()).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        Throwable throwable = ex;

        if (ex.getRootCause() instanceof IllegalArgumentException) {
            throwable = ex.getRootCause();
        }

        final var apiError = createApiError(ExtApiMyVehicleConfigurationsErrorCode.INVALID_PARAMETER, request.getDescription(false),
                throwable.getMessage());

        logger.error(apiError, ex);

        return ResponseEntity.status(apiError.getStatus()).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var apiError =
                createApiError(ExtApiMyVehicleConfigurationsErrorCode.INVALID_PARAMETER, request.getDescription(false), ex.getMessage());

        logger.error(apiError, ex);

        return ResponseEntity.status(apiError.getStatus()).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var apiError =
                createApiError(ExtApiMyVehicleConfigurationsErrorCode.INVALID_PARAMETER, request.getDescription(false), ex.getMessage());

        logger.error(apiError, ex);

        return ResponseEntity.status(apiError.getStatus()).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(apiError);
    }
}
