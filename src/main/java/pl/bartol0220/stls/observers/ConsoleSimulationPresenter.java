package pl.bartol0220.stls.observers;

import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleSimulationPresenter implements SimulationObserver {
    private final Map<Integer, List<Vehicle>> stepResults = new HashMap<>();
    private int StepCounter = 1;

    @Override
    public void newStep(List<Vehicle> leftVehicles, Intersection intersection) {
        synchronized (System.out) {
            System.out.println("####################\nStep: " + StepCounter + "\n####################");
            System.out.println(intersection);
            if (!leftVehicles.isEmpty()) {
                System.out.print("----------------\nVehicles left:");
                leftVehicles.forEach(vehicle -> System.out.print(" " + vehicle));
                System.out.println();
            }
            System.out.println("####################\n");
        }
        if (!leftVehicles.isEmpty()) {
            stepResults.put(StepCounter, leftVehicles);
        }
        StepCounter++;
    }

    @Override
    public void newVehicle(String vehicleId, RoadsDirection startRoad, RoadsDirection endRoad) {
        synchronized (System.out) {
            System.out.println("---------------");
            System.out.println("New Vehicle: " +  vehicleId + " | " + startRoad + " -> " + endRoad);
            System.out.println("---------------\n");
        }
    }

    @Override
    public void endOfSimulation() {
        synchronized (System.out) {
            System.out.println("####################\nEnd of Simulation\n####################\nList of vehicles that left intersection for every step:\n");
            for (int i : stepResults.keySet()) {
                System.out.print(i + ":");
                stepResults.get(i).forEach(vehicle -> System.out.print(" " + vehicle));
                System.out.println();
            }
            System.out.println("####################\n");
        }
    }
}
