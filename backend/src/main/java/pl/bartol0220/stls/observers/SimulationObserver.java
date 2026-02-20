package pl.bartol0220.stls.observers;

import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.List;

public interface SimulationObserver {
    void newStep(List<Vehicle> leftVehicles, Intersection intersection);

    void newVehicle(String vehicleId, RoadsDirection startRoad, RoadsDirection endRoad);

    void endOfSimulation();
}
