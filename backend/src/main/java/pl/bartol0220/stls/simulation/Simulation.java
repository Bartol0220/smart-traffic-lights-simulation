package pl.bartol0220.stls.simulation;

import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.lightControllers.AbstractLightController;
import pl.bartol0220.stls.model.lightControllers.phase.LightPhaseSequence;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Car;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.List;

public class Simulation {
    private final Intersection intersection;
    private final AbstractLightController lightController;

    public Simulation(Intersection intersection, AbstractLightController lightController) {
        this.intersection = intersection;
        this.lightController = lightController;

        this.lightController.initLightController();
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
