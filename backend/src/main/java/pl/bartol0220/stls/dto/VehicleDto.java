package pl.bartol0220.stls.dto;

import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.util.VehicleType;

public record VehicleDto(
        String id,
        RoadsDirection startRoad,
        RoadsDirection endRoad,
        VehicleType type
) {}
