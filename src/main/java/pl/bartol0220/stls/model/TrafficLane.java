package pl.bartol0220.stls.model;

import pl.bartol0220.stls.model.lightControllers.TrafficLight;
import pl.bartol0220.stls.model.util.LaneType;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.*;

public class TrafficLane {
    private final int index;
    private final int lanePriority;
    private final RoadsDirection entryDirection;
    private final Set<RoadsDirection> exitDirections;
    private final Set<LaneType> laneType;
    private final Queue<Vehicle> vehicles = new LinkedList<>();
    private final TrafficLight trafficLight = new TrafficLight();
    private int vehiclesPriority = 0;
    private int numberOfEmergencyVehicles = 0;

    public TrafficLane(int index, int lanePriority, RoadsDirection entryDirection) {
        this.index = index;
        this.lanePriority = lanePriority;
        this.entryDirection = entryDirection;
        exitDirections = EnumSet.noneOf(RoadsDirection.class);
        laneType = EnumSet.noneOf(LaneType.class);
    }

    public void addDirection(RoadsDirection exitDirection) {
        exitDirections.add(exitDirection);
        laneType.add(exitDirection.getLaneType(entryDirection));
    }

    public void removeDirection(RoadsDirection exitDirection) {
        exitDirections.remove(exitDirection);
        laneType.remove(exitDirection.getLaneType(entryDirection));
    }

    public int getLanePriority() {
        return lanePriority;
    }

    public RoadsDirection getEntryDirection() {
        return entryDirection;
    }

    public Set<RoadsDirection> getExitDirections() {
        return Collections.unmodifiableSet(exitDirections);
    }

    public Set<LaneType> getLaneType() {
        return Collections.unmodifiableSet(laneType);
    }

    public int getVehiclesPriority() {
        return vehiclesPriority;
    }

    public int getNumberOfVehicles() {
        return vehicles.size();
    }

    public boolean containsEmergencyVehicle() {
        return numberOfEmergencyVehicles > 0;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehiclesPriority = vehiclesPriority + vehicle.getPriority();
        numberOfEmergencyVehicles += vehicle.isEmergencyVehicle() ? 1 : 0;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public Optional<Vehicle> step() {
        if (trafficLight.canVehiclePass()){
            Optional<Vehicle> vehicle = Optional.ofNullable(vehicles.poll());
            vehiclesPriority -= vehicle.map(Vehicle::getPriority).orElse(0);
            numberOfEmergencyVehicles -= vehicle.map(v -> v.isEmergencyVehicle() ? 1 : 0).orElse(0);
            return vehicle;
        }
        return Optional.empty();
    }

    public void changeLightToGreen() {
        trafficLight.changeLightToGreen();
    }

    public void changeLightToRed() {
        trafficLight.changeLightToRed();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrafficLane that)) return false;
        return index == that.index && entryDirection == that.entryDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, entryDirection);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("    Lane: ");
        for (LaneType laneType : laneType) {
            sb.append(laneType).append(" ");
        }
        sb.append("\n      Vehicles: ");
        for (Vehicle vehicle : vehicles) {
            sb.append(vehicle).append(" ");
        }
        sb.append("\n      ").append(trafficLight);
        return sb.toString();
    }

    public int getHighestDirectionWeight() {
        return laneType.stream()
                .mapToInt(TrafficLane::getLaneTypeWeight)
                .max()
                .orElse(0);
    }

    public int getLowestDirectionWeight() {
        return laneType.stream()
                .mapToInt(TrafficLane::getLaneTypeWeight)
                .min()
                .orElse(Integer.MAX_VALUE);
    }

    private static int getLaneTypeWeight(LaneType type) {
        return switch (type) {
            case U_TURN -> 1;
            case LEFT -> 2;
            case STRAIGHT -> 3;
            case RIGHT -> 4;
        };
    }

    public static final Comparator<TrafficLane> VISUAL_ORDER = (lane1, lane2) -> {
        List<LaneType> types1 = new ArrayList<>(lane1.getLaneType());
        List<LaneType> types2 = new ArrayList<>(lane2.getLaneType());

        types1.sort(Comparator.comparingInt(TrafficLane::getLaneTypeWeight));
        types2.sort(Comparator.comparingInt(TrafficLane::getLaneTypeWeight));

        int size = Math.min(types1.size(), types2.size());
        for (int i = 0; i < size; i++) {
            int weight1 = getLaneTypeWeight(types1.get(i));
            int weight2 = getLaneTypeWeight(types2.get(i));

            if (weight1 != weight2) {
                return Integer.compare(weight1, weight2);
            }
        }

        return Integer.compare(types1.size(), types2.size());
    };
}
