package pl.bartol0220.stls.model;

import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class TrafficLane {
    private final RoadsDirection exitDirection;
    private final Queue<Vehicle> vehicles = new LinkedList<Vehicle>();
    private int vehiclesPriority = 0;

    public TrafficLane(RoadsDirection exitDirection) {
        this.exitDirection = exitDirection;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehiclesPriority = vehiclesPriority + vehicle.getPriority();
    }

    public Optional<Vehicle> nextVehicle() {
        Optional<Vehicle> vehicle = Optional.ofNullable(vehicles.poll());
        vehiclesPriority -= vehicle.map(Vehicle::getPriority).orElse(0);
        return vehicle;
    }
}
