package pl.bartol0220.stls.dto;

import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.List;

public record AddLaneDto(
        RoadsDirection entryDirection,
        List<RoadsDirection> exitDirections,
        int lanePriority,
        boolean addOpositeLane
) {}
