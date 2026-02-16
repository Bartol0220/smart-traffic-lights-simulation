package pl.bartol0220.stls.model;

import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
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

    public void addTrafficLane(List<RoadsDirection> exitDirections, int lanePriority) {
        TrafficLane trafficLane = new TrafficLane(trafficLaneIndex, lanePriority, entryDirection);
        trafficLaneIndex++;
        for (RoadsDirection exitDirection : exitDirections) {
            trafficLane.addDirection(exitDirection);
        }
        trafficLanes.add(trafficLane);
    }

    public void addTrafficLane(List<RoadsDirection> exitDirections) {
        addTrafficLane(exitDirections, roadPriority);
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

    public List<Vehicle> step() {
        LinkedList<Vehicle> leftVehicles = new LinkedList<>();
        for (TrafficLane trafficLane : trafficLanes) {
            trafficLane.step().ifPresent(leftVehicles::add);
        }
        return leftVehicles;
    }

    public void newVehicle(Vehicle vehicle) throws IllegalVehicleDestination {
        TrafficLane trafficLane = findBestTrafficLane(vehicle.getEndRoad());
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
