package pl.bartol0220.stls.dto;

import pl.bartol0220.stls.model.util.LaneType;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.List;
import java.util.Set;

public record TrafficLaneDto(
        int index,
        int lanePriority,
        RoadsDirection entryDirection,
        Set<RoadsDirection> exitDirections,
        Set<LaneType> laneType,
        List<VehicleDto> vehicles,
        TrafficLightDto trafficLight
) {}
