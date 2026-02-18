package pl.bartol0220.stls.simulation;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.lightControllers.AbstractLightController;
import pl.bartol0220.stls.model.lightControllers.LightPhaseSequence;
import pl.bartol0220.stls.model.lightControllers.VehiclesPriorityLightController;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Car;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.List;
import java.util.Map;

public class Simulation {
    private final Intersection intersection;
    private final AbstractLightController lightController;

    public Simulation(Intersection intersection, AbstractLightController lightController) {
        this.intersection = intersection;
        this.lightController = lightController;

        this.lightController.initLightController();
    }

    public Simulation() throws EmptyLightPhases, InvalidTrafficLaneDirectionException {
        intersection = new Intersection(Map.of(
                RoadsDirection.NORTH, 4,
                RoadsDirection.SOUTH, 4,
                RoadsDirection.EAST, 1,
                RoadsDirection.WEST, 1
        ));
        
        Road nortRoad = intersection.getRoad(RoadsDirection.NORTH);
        nortRoad.addTrafficLane(List.of(RoadsDirection.EAST), 2);
        nortRoad.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST));

        Road southRoad = intersection.getRoad(RoadsDirection.SOUTH);
        southRoad.addTrafficLane(List.of(RoadsDirection.WEST), 2);
        southRoad.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST));

        Road eastRoad = intersection.getRoad(RoadsDirection.EAST);
        eastRoad.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST, RoadsDirection.NORTH));

        Road westRoad = intersection.getRoad(RoadsDirection.WEST);
        westRoad.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH));

        lightController = new VehiclesPriorityLightController(intersection);
        lightController.initLightController();
    }

    public List<Vehicle> step() {
        lightController.step();
        return intersection.step();
    }

    public void addVehicle(Vehicle vehicle) throws IllegalVehicleDestination {
        intersection.getRoad(vehicle.getStartRoad()).newVehicle(vehicle);
    }

    public void addVehicle(String vehicleId, RoadsDirection startRoad, RoadsDirection endRoad) throws IllegalVehicleDestination {
        Vehicle vehicle = new Car(vehicleId, startRoad, endRoad);
        addVehicle(vehicle);
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public LightPhaseSequence getLightPhaseSequence() {
        return lightController.getLightPhaseSequence();
    }
}
