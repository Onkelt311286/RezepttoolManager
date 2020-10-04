/*
 * Copyright (C) 2019 Volkswagen AG, Hannover, Germany.
 */
package vwg.vwn.ccp.extapi.myvehicleconfigurations.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vwg.vwn.ccp.dis.carconfig.model.AllVehicleConfigurationsForStorage;
import vwg.vwn.ccp.dis.carconfig.model.VehicleConfigurationForStorage;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.client.carconfig.DisCarConfigDataClient;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.data.MyConfigurationsTestDataFactory;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.AllMyVehicleConfigurations;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.MyVehicleConfiguration;
import vwg.vwn.ccp.http.fail.ApiException;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class MyConfigurationsServiceTest {

    @Mock
    private DisCarConfigDataClient disCarConfigMock;

    @InjectMocks
    private MyVehicleConfigurationsService objectUnderTest;

    @Test
    public void fetchAllMyConfigurations_404FromDis_returns204() {
        when(disCarConfigMock.fetchAllMyVehicleConfigurations()).thenReturn(ResponseEntity.notFound().build());
        final ResponseEntity<AllMyVehicleConfigurations> result = objectUnderTest.fetchAllMyConfigurations();
        assertThat(result.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

    @Test
    public void fetchAllMyConfigurations_nullConfigsStored_returns204() {
        final AllVehicleConfigurationsForStorage disResponse =
                MyConfigurationsTestDataFactory.createNullConfigsAllVehicleConfigurationsForStorage();
        when(disCarConfigMock.fetchAllMyVehicleConfigurations()).thenReturn(ResponseEntity.ok(disResponse));
        final ResponseEntity<AllMyVehicleConfigurations> result = objectUnderTest.fetchAllMyConfigurations();
        assertThat(result.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

    @Test
    public void fetchAllMyConfigurations_noConfigsStored_returns204() {
        final AllVehicleConfigurationsForStorage disResponse =
                MyConfigurationsTestDataFactory.createEmptyConfigsAllVehicleConfigurationsForStorage();
        when(disCarConfigMock.fetchAllMyVehicleConfigurations()).thenReturn(ResponseEntity.ok(disResponse));
        final ResponseEntity<AllMyVehicleConfigurations> result = objectUnderTest.fetchAllMyConfigurations();
        assertThat(result.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

    @Test
    public void fetchAllMyConfigurations_configsStored_returns200() {
        final AllVehicleConfigurationsForStorage disResponse =
                MyConfigurationsTestDataFactory.createOneConfigAllVehicleConfigurationsForStorage();
        final AllMyVehicleConfigurations expectedConfig =
                MyConfigurationsTestDataFactory.createOneConfigAllMyVehicleConfigurations();


        when(disCarConfigMock.fetchAllMyVehicleConfigurations()).thenReturn(ResponseEntity.ok(disResponse));
        final ResponseEntity<AllMyVehicleConfigurations> result = objectUnderTest.fetchAllMyConfigurations();
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), not(is(nullValue())));
        assertThat(result.getBody(), is(expectedConfig));
    }

    @Test(expected = ApiException.class)
    public void fetchMyConfiguration_404FromDis_throwsApiException() {
        when(disCarConfigMock.fetchMyVehicleConfiguration(any())).thenReturn(ResponseEntity.notFound().build());
        objectUnderTest.fetchMyConfiguration(MyConfigurationsTestDataFactory.KNOWN_CAR_ID);
    }

    @Test
    public void fetchMyConfiguration_foundConfig_returns200() {
        final VehicleConfigurationForStorage disResponse =
                MyConfigurationsTestDataFactory.createValidVehicleConfigurationForStorage();
        final MyVehicleConfiguration expectedConfig =
                MyConfigurationsTestDataFactory.createCompleteMyVehicleConfiguration();


        when(disCarConfigMock.fetchMyVehicleConfiguration(any())).thenReturn(ResponseEntity.ok(disResponse));
        final ResponseEntity<MyVehicleConfiguration> result =
                objectUnderTest.fetchMyConfiguration(MyConfigurationsTestDataFactory.KNOWN_CAR_ID);
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), not(is(nullValue())));
        assertThat(result.getBody(), is(expectedConfig));
    }

    @Test
    public void storeOrUpdateConfiguration_validConfig_returns200() {
        final MyVehicleConfiguration requestedConfig =
                MyConfigurationsTestDataFactory.createCompleteMyVehicleConfiguration();
        final VehicleConfigurationForStorage mappedConfig =
                MyConfigurationsTestDataFactory.createValidVehicleConfigurationForStorage();
        when(disCarConfigMock.addOrUpdateVehicleConfiguration(mappedConfig)).thenReturn(ResponseEntity.ok().build());
        final ResponseEntity<Void> result = objectUnderTest.storeOrUpdateConfiguration(requestedConfig);
        verify(disCarConfigMock, times(1)).addOrUpdateVehicleConfiguration(mappedConfig);
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test(expected = ApiException.class)
    public void deleteMyConfiguration_404FromDis_throwsApiException() {
        when(disCarConfigMock.deleteVehicleConfiguration(any())).thenReturn(ResponseEntity.notFound().build());
        objectUnderTest.deleteMyConfiguration(MyConfigurationsTestDataFactory.KNOWN_CAR_ID);
    }

    @Test
    public void deleteMyConfiguration_foundConfig_returns200() {
        when(disCarConfigMock.deleteVehicleConfiguration(any())).thenReturn(ResponseEntity.ok().build());
        final ResponseEntity<Void> result =
                objectUnderTest.deleteMyConfiguration(MyConfigurationsTestDataFactory.KNOWN_CAR_ID);
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }
}
