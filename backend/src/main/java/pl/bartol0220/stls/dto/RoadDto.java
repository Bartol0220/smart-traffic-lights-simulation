package pl.bartol0220.stls.dto;

import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.List;

public record RoadDto(
        RoadsDirection entryDirection,
        List<TrafficLaneDto> trafficLanes
) {}
