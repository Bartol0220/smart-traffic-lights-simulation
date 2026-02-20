package pl.bartol0220.stls.dto;

import java.util.List;

public record SimulationDto(
        IntersectionDto intersection,
        int step,
        List<VehicleDto> leftVehicles
) {}
