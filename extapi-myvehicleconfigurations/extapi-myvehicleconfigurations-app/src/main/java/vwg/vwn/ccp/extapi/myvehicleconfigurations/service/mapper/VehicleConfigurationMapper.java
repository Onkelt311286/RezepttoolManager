package vwg.vwn.ccp.extapi.myvehicleconfigurations.service.mapper;

import java.util.stream.Collectors;
import vwg.vwn.ccp.dis.carconfig.model.AllVehicleConfigurationsForStorage;
import vwg.vwn.ccp.dis.carconfig.model.VehicleConfigurationForStorage;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.AllMyVehicleConfigurations;
import vwg.vwn.ccp.extapi.myvehicleconfigurations.model.MyVehicleConfiguration;

public class VehicleConfigurationMapper {

    public static AllMyVehicleConfigurations mapDisConfigToBffConfig(
            AllVehicleConfigurationsForStorage disVehicleConfigs) {
        // @formatter:off
        return new AllMyVehicleConfigurations()
                .configurations(disVehicleConfigs.getVehicleConfigurations().parallelStream()
                        .map(disConfig -> VehicleConfigurationMapper.mapDisConfigToBffConfig(disConfig))
                        .collect(Collectors.toList()));
        // @formatter:on
    }

    public static MyVehicleConfiguration mapDisConfigToBffConfig(VehicleConfigurationForStorage disVehicleConfig) {
        // @formatter:off
        return new MyVehicleConfiguration()
                .carId(disVehicleConfig.getCarId())
                .carlineId(disVehicleConfig.getCarlineId())
                .createdAt(disVehicleConfig.getCreatedAt())
                .updatedAt(disVehicleConfig.getUpdatedAt())
                .exteriorId(disVehicleConfig.getExteriorId())
                .interiorId(disVehicleConfig.getInteriorId())
                .isConverted(disVehicleConfig.getIsConverted())
                .modelId(disVehicleConfig.getModelId())
                .modelVersion(disVehicleConfig.getModelVersion())
                .modelYear(disVehicleConfig.getModelYear())
                .name(disVehicleConfig.getName())
                .salesgroupId(disVehicleConfig.getSalesgroupId())
                .tenant(disVehicleConfig.getTenant())
                .options(disVehicleConfig.getOptions());
        // @formatter:on
    }

    public static VehicleConfigurationForStorage mapBffConfigToDisConfig(MyVehicleConfiguration myVehicleConfig) {
        // @formatter:off
        return new VehicleConfigurationForStorage()
                .carId(myVehicleConfig.getCarId())
                .carlineId(myVehicleConfig.getCarlineId())
                .createdAt(myVehicleConfig.getCreatedAt())
                .updatedAt(myVehicleConfig.getUpdatedAt())
                .exteriorId(myVehicleConfig.getExteriorId())
                .interiorId(myVehicleConfig.getInteriorId())
                .isConverted(myVehicleConfig.getIsConverted())
                .modelId(myVehicleConfig.getModelId())
                .modelVersion(myVehicleConfig.getModelVersion())
                .modelYear(myVehicleConfig.getModelYear())
                .name(myVehicleConfig.getName())
                .salesgroupId(myVehicleConfig.getSalesgroupId())
                .tenant(myVehicleConfig.getTenant())
                .options(myVehicleConfig.getOptions());
        // @formatter:on
    }
    
    private VehicleConfigurationMapper() {}
}
