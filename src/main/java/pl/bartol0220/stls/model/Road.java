package pl.bartol0220.stls.model;

import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Road {
    private final int roadPriority;
    private final RoadsDirection entryDirection;
    private final List<TrafficLane> trafficLanes = new ArrayList<>();
    private int trafficLaneIndex = 0;

    public Road(RoadsDirection entryDirection, int roadPriority) {
        this.entryDirection = entryDirection;
        this.roadPriority = roadPriority;
    }

    public void addTrafficLane(List<RoadsDirection> exitDirections) {
        TrafficLane trafficLane = new TrafficLane(trafficLaneIndex, roadPriority, entryDirection);
        trafficLaneIndex++;
        for (RoadsDirection exitDirection : exitDirections) {
            trafficLane.addDirection(exitDirection);
        }
        trafficLanes.add(trafficLane);
    }

    public List<TrafficLane> getTrafficLanes() {
        return new ArrayList<>(trafficLanes);
    }

    private TrafficLane findBestTrafficLane(RoadsDirection destination) throws IllegalVehicleDestination {
        return trafficLanes
                .stream()
                .filter(lane -> lane.getExitDirections().contains(destination))
                .min(Comparator.comparingInt(TrafficLane::getVehiclesPriority))
                .orElseThrow(() -> new IllegalVehicleDestination(destination));
    }

    public void newVehicle(Vehicle vehicle) throws IllegalVehicleDestination {
        TrafficLane trafficLane = findBestTrafficLane(entryDirection);
        trafficLane.addVehicle(vehicle);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("  Road-" + entryDirection + "\n");
        for (TrafficLane trafficLane : trafficLanes) {
            sb.append(trafficLane).append("\n");
        }
        return sb.toString();
    }
}
