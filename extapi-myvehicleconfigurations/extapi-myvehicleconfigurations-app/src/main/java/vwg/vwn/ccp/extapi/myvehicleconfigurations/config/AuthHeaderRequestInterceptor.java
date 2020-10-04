/*
 * Copyright (C) 2019 Volkswagen AG, Hannover, Germany.
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.config;

import static java.util.Arrays.asList;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthHeaderRequestInterceptor implements ClientHttpRequestInterceptor {

    private final HttpServletRequest servletRequest;

    public AuthHeaderRequestInterceptor(HttpServletRequest internalToken) {
        servletRequest = internalToken;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        final String authHeaderValue = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaderValue != null) {
            request.getHeaders().put(HttpHeaders.AUTHORIZATION, asList(authHeaderValue));
        }
        return execution.execute(request, body);
    }
}
