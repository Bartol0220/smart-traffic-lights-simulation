package pl.bartol0220.stls.dto;

import pl.bartol0220.stls.model.util.DelegateLightControllerType;

public record UpdateConfigDto(
        Integer maxPhaseTime,
        DelegateLightControllerType controllerType,
        Boolean emergencyLightController
) {}
