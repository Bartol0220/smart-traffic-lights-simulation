package pl.bartol0220.stls.dto;

import pl.bartol0220.stls.model.util.DelegateLightControllerType;

public record SimulationConfigDto(
      IntersectionDto intersection,
      boolean isEmergencyLightController,
      int maxPhaseTime,
      DelegateLightControllerType type
) {}
