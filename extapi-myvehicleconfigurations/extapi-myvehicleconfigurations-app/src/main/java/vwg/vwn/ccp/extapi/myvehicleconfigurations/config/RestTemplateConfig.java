/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.client.carconfig.DisCarConfigDataClientResponseErrorHandler;

@Configuration
public class RestTemplateConfig {

    private final int connectTimeout;
    private final int readTimeout;

    private final ObjectMapper objectMapper;

    public RestTemplateConfig(@Value("${ccp.networking.internal.connect-timeout-millis}") int connectTimeout,
            @Value("${ccp.networking.internal.read-timeout-millis}") int readTimeout, ObjectMapper objectMapper) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.objectMapper = objectMapper;
    }

    @Bean
    public RestTemplate disCarconfigDataRestTemplate(
            @Value("https://dis-carconfig-data-${ccp.stage}.${ccp.networking.internal.services-domain}") String rootUri,
            RestTemplateBuilder builder, DisCarConfigDataClientResponseErrorHandler errorHandler,
            AuthHeaderRequestInterceptor requestInterceptor) {

        // @formatter:off
        return builder
                .rootUri(rootUri)
                .additionalInterceptors(requestInterceptor)
                .requestFactory(this::requestFactoryWithoutProxy)
                .errorHandler(new DisCarConfigDataClientResponseErrorHandler(objectMapper))
                .build();
        // @formatter:on
    }

    @Bean
    ClientHttpRequestFactory requestFactoryWithoutProxy() {
        final var requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(connectTimeout);
        requestConfigBuilder.setSocketTimeout(readTimeout);

        // @formatter:off
        final var httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfigBuilder.build())
                .disableCookieManagement()
                .disableRedirectHandling()
                .build();
        // @formatter:on

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
