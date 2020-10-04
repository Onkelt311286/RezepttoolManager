/**
 * Copyright (C) 2020 Volkswagen AG, Hannover, Germany
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static vwg.vwn.ccp.testutils.common.JsonMapper.asJson;
import static vwg.vwn.ccp.testutils.common.MockRequestValidators.isUrlPathMatching;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import vwg.vwn.ccp.dis.carconfig.model.AllVehicleConfigurationsForStorage;
import vwg.vwn.ccp.dis.carconfig.model.VehicleConfigurationForStorage;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.OpenAPI2SpringBoot;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.data.MyConfigurationsTestDataFactory;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.AllMyVehicleConfigurations;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.MyVehicleConfiguration;
import vwg.vwn.ccp.http.fail.ApiError;
import vwg.vwn.ccp.testutils.common.MockRequestValidators;
import vwg.vwn.ccp.testutils.runner.MockedVCapTestRunner;

@ActiveProfiles("test")
@RunWith(MockedVCapTestRunner.class)
@SpringBootTest(classes = OpenAPI2SpringBoot.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MyVehiclesApiControllerTest {

    private static final String MYCONFIGURATIONS_PATH = "/myConfigurations";

    private static final String DIS_CARCONFIG_DATA_CONFIGURATIONS_PATH = "/myvehicleconfiguration";
    private static final String DIS_CARCONFIG_DATA_CONFIGURE_PATH = "/myvehicleconfiguration/configure";

    private static MockWebServer disCarConfigMockServer;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MockMvc objectUnderTest;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeClass
    public static void mockWebServerStartUp() throws IOException {
        disCarConfigMockServer = new MockWebServer();
        disCarConfigMockServer.start();
    }

    @AfterClass
    public static void mockWebServerTearDown() throws IOException {
        disCarConfigMockServer.shutdown();
    }

    @Before
    public void setUp() {
        final HttpUrl vehicleUrl = disCarConfigMockServer.url("");
        final DefaultUriBuilderFactory vehicleUriBuilderFactory = new DefaultUriBuilderFactory(vehicleUrl.toString());
        context.getBean("disCarconfigDataRestTemplate", RestTemplate.class)
                .setUriTemplateHandler(vehicleUriBuilderFactory);
    }

    @Test
    public void fetchAllMyConfigurations_oneStoredVehicle_returnStatus200WithMappedBody() throws Exception {

        final AllVehicleConfigurationsForStorage disResponse =
                MyConfigurationsTestDataFactory.createOneConfigAllVehicleConfigurationsForStorage();
        final AllMyVehicleConfigurations expectedConfig =
                MyConfigurationsTestDataFactory.createOneConfigAllMyVehicleConfigurations();

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(asJson(disResponse, objectMapper)));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(get(MYCONFIGURATIONS_PATH))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON))
               .andExpect(content().json(asJson(expectedConfig, objectMapper)));
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodGetMatching)
                .matches(request -> isUrlPathMatching(request, DIS_CARCONFIG_DATA_CONFIGURATIONS_PATH));
    }

    @Test
    public void fetchAllMyConfigurations_notFound_returnStatus204() throws Exception {

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON)
                .setBody(asJson(new ApiError().status(404), objectMapper)));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(get(MYCONFIGURATIONS_PATH))
               .andDo(log())
               .andExpect(status().isNoContent());
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodGetMatching)
                .matches(request -> isUrlPathMatching(request, DIS_CARCONFIG_DATA_CONFIGURATIONS_PATH));
    }

    @Test
    public void fetchMyConfiguration_foundVehicle_returnStatus200WithMappedBody() throws Exception {

        final VehicleConfigurationForStorage disResponse =
                MyConfigurationsTestDataFactory.createValidVehicleConfigurationForStorage();
        final MyVehicleConfiguration expectedConfig =
                MyConfigurationsTestDataFactory.createCompleteMyVehicleConfiguration();

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(asJson(disResponse, objectMapper)));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(get(MYCONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON))
               .andExpect(content().json(asJson(expectedConfig, objectMapper)));
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodGetMatching)
                .matches(request -> isUrlPathMatching(request,
                        DIS_CARCONFIG_DATA_CONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID));
    }

    @Test
    public void fetchMyConfiguration_notFoundVehicle_returnStatus204() throws Exception {

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON)
                .setBody(asJson(new ApiError().status(404), objectMapper)));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(get(MYCONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID))
               .andDo(log())
               .andExpect(status().isNotFound())
               .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodGetMatching)
                .matches(request -> isUrlPathMatching(request,
                        DIS_CARCONFIG_DATA_CONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID));
    }

    @Test
    public void storeOrUpdateConfiguration_validConfig_returnStatus200() throws Exception {

        final VehicleConfigurationForStorage disResponse =
                MyConfigurationsTestDataFactory.createValidVehicleConfigurationForStorage();
        final MyVehicleConfiguration configRequest =
                MyConfigurationsTestDataFactory.createCompleteMyVehicleConfiguration();

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(asJson(disResponse, objectMapper)));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(post(MYCONFIGURATIONS_PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(configRequest, objectMapper)))
                .andDo(log())
                .andExpect(status().isOk());
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodPostMatching)
                .matches(request -> isUrlPathMatching(request, DIS_CARCONFIG_DATA_CONFIGURE_PATH));
    }

    @Test
    public void storeOrUpdateConfiguration_invalidConfig_returnStatus400() throws Exception {

        final MyVehicleConfiguration configRequest =
                MyConfigurationsTestDataFactory.createEmptyMyVehicleConfiguration();

        // @formatter:off
        objectUnderTest.perform(post(MYCONFIGURATIONS_PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(configRequest, objectMapper)))
                .andDo(log())
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void storeOrUpdateConfiguration_minimumValidConfig_returnStatus200() throws Exception {

        final VehicleConfigurationForStorage disResponse =
                MyConfigurationsTestDataFactory.createValidVehicleConfigurationForStorage();
        final MyVehicleConfiguration configRequest =
                MyConfigurationsTestDataFactory.createMinimumValidMyVehicleConfiguration();

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(asJson(disResponse, objectMapper)));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(post(MYCONFIGURATIONS_PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(configRequest, objectMapper)))
                .andDo(log())
                .andExpect(status().isOk());
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodPostMatching)
                .matches(request -> isUrlPathMatching(request, DIS_CARCONFIG_DATA_CONFIGURE_PATH));
    }

    @Test
    public void deleteMyConfiguration_knownCarId_returnStatus200() throws Exception {

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(delete(MYCONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(log())
                .andExpect(status().isOk());
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodDeleteMatching)
                .matches(request -> isUrlPathMatching(request, DIS_CARCONFIG_DATA_CONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID));
    }
    
    @Test
    public void deleteMyConfiguration_unknownCarId_returnStatus404() throws Exception {

        // @formatter:off
        disCarConfigMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON)
                .setBody(asJson(new ApiError().status(404), objectMapper)));
        // @formatter:on

        // @formatter:off
        objectUnderTest.perform(delete(MYCONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andDo(log())
                .andExpect(status().isNotFound());
        // @formatter:on

        final RecordedRequest recordedRequest = disCarConfigMockServer.takeRequest(3L, TimeUnit.SECONDS);

        assertThat(recordedRequest).matches(MockRequestValidators::isHttpMethodDeleteMatching)
                .matches(request -> isUrlPathMatching(request, DIS_CARCONFIG_DATA_CONFIGURATIONS_PATH + "/" + MyConfigurationsTestDataFactory.KNOWN_CAR_ID));
    }
}
