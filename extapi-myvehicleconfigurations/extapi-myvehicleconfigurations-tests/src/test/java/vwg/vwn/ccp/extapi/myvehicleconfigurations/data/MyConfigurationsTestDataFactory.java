package vwg.vwn.ccp.extapi.myvehicleconfigurations.data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import vwg.vwn.ccp.dis.carconfig.model.AllVehicleConfigurationsForStorage;
import vwg.vwn.ccp.dis.carconfig.model.VehicleConfigurationForStorage;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.fail.ExtApiMyVehicleConfigurationsErrorCode;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.AllMyVehicleConfigurations;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.MyVehicleConfiguration;
import vwg.vwn.ccp.http.fail.ApiError;

public class MyConfigurationsTestDataFactory {

    public static final String KNOWN_CAR_ID = "SomeCarId";
    public static final String UNKNOWN_CAR_ID = "UnknownCarId";
    
    public static AllVehicleConfigurationsForStorage createNullConfigsAllVehicleConfigurationsForStorage() {
        return new AllVehicleConfigurationsForStorage();
    }

    public static AllVehicleConfigurationsForStorage createEmptyConfigsAllVehicleConfigurationsForStorage() {
        return new AllVehicleConfigurationsForStorage().vehicleConfigurations(new ArrayList<>());
    }

    public static AllVehicleConfigurationsForStorage createOneConfigAllVehicleConfigurationsForStorage() {
        return new AllVehicleConfigurationsForStorage()
                .vehicleConfigurations(Arrays.asList(createValidVehicleConfigurationForStorage()));
    }

    public static VehicleConfigurationForStorage createValidVehicleConfigurationForStorage() {
        // @formatter:off
        return new VehicleConfigurationForStorage()
                .carId("SomeCarId")
                .carlineId("Some carlineId")
                .createdAt("Some createdAt time")
                .updatedAt("Some updatedAt time")
                .exteriorId("Some exteriorId")
                .interiorId("Some interiorId")
                .isConverted(true)
                .modelId("Some modelId")
                .modelVersion("Some modelVersion")
                .modelYear("Some modelYear")
                .name("Some name")
                .salesgroupId("Some salesgroupId")
                .tenant("Some tenant")
                .options(Arrays.asList("Some Option1"));
        // @formatter:on
    }
    
    public static VehicleConfigurationForStorage createEmptyVehicleConfigurationForStorage() {
        return new VehicleConfigurationForStorage();
    }
    
    public static AllMyVehicleConfigurations createOneConfigAllMyVehicleConfigurations() {
        return new AllMyVehicleConfigurations()
                .configurations(Arrays.asList(createCompleteMyVehicleConfiguration()));
    }
    
    public static MyVehicleConfiguration createCompleteMyVehicleConfiguration() {
        // @formatter:off
        return new MyVehicleConfiguration()
                .carId("SomeCarId")
                .carlineId("Some carlineId")
                .createdAt("Some createdAt time")
                .updatedAt("Some updatedAt time")
                .exteriorId("Some exteriorId")
                .interiorId("Some interiorId")
                .isConverted(true)
                .modelId("Some modelId")
                .modelVersion("Some modelVersion")
                .modelYear("Some modelYear")
                .name("Some name")
                .salesgroupId("Some salesgroupId")
                .tenant("Some tenant")
                .options(Arrays.asList("Some Option1"));
        // @formatter:on
    }
    
    public static MyVehicleConfiguration createEmptyMyVehicleConfiguration() {
        return new MyVehicleConfiguration();
    }
    
    public static MyVehicleConfiguration createMinimumValidMyVehicleConfiguration() {
        // @formatter:off
        return new MyVehicleConfiguration()
                .carlineId("Some carlineId")
                .exteriorId("Some exteriorId")
                .interiorId("Some interiorId")
                .modelId("Some modelId")
                .modelVersion("Some modelVersion")
                .modelYear("Some modelYear")
                .name("Some name")
                .salesgroupId("Some salesgroupId")
                .tenant("Some tenant");
        // @formatter:on
    }
}
