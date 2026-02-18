package pl.bartol0220.stls.model;

import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.*;

public class Intersection {
    private final Map<RoadsDirection, Road> roads = new EnumMap<>(RoadsDirection.class);

    public Intersection() {
        for (RoadsDirection direction : RoadsDirection.values()){
            roads.put(direction, new Road(direction, 1));
        }
    }

    public Intersection(Map<RoadsDirection, Integer> priorities) {
        for (RoadsDirection direction : RoadsDirection.values()){
            roads.put(direction, new Road(direction, priorities.get(direction)));
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

    public List<Vehicle> step() {
        List<Vehicle> leftVehicles = new LinkedList<>();
        for (RoadsDirection direction : RoadsDirection.values()) {
            leftVehicles.addAll(roads.get(direction).step());
        }
        return leftVehicles;
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
