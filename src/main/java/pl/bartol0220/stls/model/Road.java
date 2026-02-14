package pl.bartol0220.stls.model;

import java.util.ArrayList;
import java.util.EnumMap;

public class Road {
    private final RoadsDirection entryDirection;
    private final int roadPriority;
    private final EnumMap<RoadsDirection, ArrayList<TrafficLane>> trafficLanes = new EnumMap<>(RoadsDirection.class);

    public Road(RoadsDirection entryDirection, int roadPriority) {
        this.entryDirection = entryDirection;
        this.roadPriority = roadPriority;
        for(RoadsDirection direction : RoadsDirection.values()){
            trafficLanes.put(direction, new ArrayList<>());
        }
    }

    public void addTrafficLane(RoadsDirection exitDirection) {
        TrafficLane trafficLane = new TrafficLane(exitDirection);
        trafficLanes.get(exitDirection).add(trafficLane);
    }
}
