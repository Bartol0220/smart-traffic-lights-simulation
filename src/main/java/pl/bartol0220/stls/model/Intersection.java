package pl.bartol0220.stls.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Intersection {
    private final Map<RoadsDirection, Road> roads = new EnumMap<>(RoadsDirection.class);

    public Intersection() {
        for (RoadsDirection direction : RoadsDirection.values()){
            roads.put(direction, new Road(direction, 1));
        }
    }

    public Road getRoad(RoadsDirection direction) {
        return roads.get(direction);
    }

    public List<TrafficLane> getAllLanes() {
        List<TrafficLane> result = new ArrayList<>();
        for (RoadsDirection direction : RoadsDirection.values()) {
            result.addAll(roads.get(direction).getTrafficLanes());
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Intersection:\n");
        for (RoadsDirection direction : RoadsDirection.values()){
            sb.append(roads.get(direction));
        }
        return sb.toString();
    }
}
