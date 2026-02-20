package pl.bartol0220.stls.model;

import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.*;

public class Road {
    private final int roadPriority;
    private final RoadsDirection entryDirection;
    private final List<TrafficLane> trafficLanes = new ArrayList<>();
    private int trafficLaneIndex = 0;

    public Road(RoadsDirection entryDirection, int roadPriority) {
        this.entryDirection = entryDirection;
        this.roadPriority = roadPriority;
    }

    private void checkForLanesCollisions() throws InvalidTrafficLaneDirectionException {
        if (trafficLanes.size() < 2) return;

        for (int i = 1; i < trafficLanes.size(); i++) {
            TrafficLane leftLane = trafficLanes.get(i - 1);
            TrafficLane rightLane = trafficLanes.get(i);

            int maxWeightLeft = leftLane.getHighestDirectionWeight();
            int minWeightRight = rightLane.getLowestDirectionWeight();

            if (maxWeightLeft > minWeightRight) {
                throw new InvalidTrafficLaneDirectionException();
            }
        }
    }

    public void addTrafficLane(List<RoadsDirection> exitDirections, int lanePriority) throws InvalidTrafficLaneDirectionException {
        TrafficLane trafficLane = new TrafficLane(trafficLaneIndex, lanePriority, entryDirection);
        for (RoadsDirection exitDirection : exitDirections) {
            trafficLane.addDirection(exitDirection);
        }
        trafficLanes.add(trafficLane);
        trafficLanes.sort(TrafficLane.VISUAL_ORDER);

        try {
            checkForLanesCollisions();
            trafficLaneIndex++;
        } catch (InvalidTrafficLaneDirectionException e) {
            trafficLanes.remove(trafficLane);
            throw e;
        }
    }

    public void addTrafficLane(List<RoadsDirection> exitDirections) throws InvalidTrafficLaneDirectionException {
        addTrafficLane(exitDirections, roadPriority);
    }

    public void removeTrafficLane(int index) {
        TrafficLane lane = new TrafficLane(index, 0, entryDirection);
        trafficLanes.remove(lane);
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
