package pl.bartol0220.stls.simulation;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.io.SimulationCommand;
import pl.bartol0220.stls.io.SimulationInput;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;
import pl.bartol0220.stls.observers.SimulationObserver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSimulationRunner implements Runnable {
    private final Simulation simulation;
    private final Path inputFilePath;
    private final List<SimulationObserver> observers = new ArrayList<>();

    public FileSimulationRunner(Simulation simulation, Path inputFilePath) {
        this.simulation = simulation;
        this.inputFilePath = inputFilePath;
    }

    @Override
    public void run() {
        JsonMapper objectMapper = JsonMapper
                .builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
        try {
            SimulationInput input = objectMapper.readValue(inputFilePath.toFile(), SimulationInput.class);
            for (SimulationCommand command : input.commands()) {
                executeCommand(command);
            }
            notifyObserversOfEndOfSimulation();
        } catch (IOException e) {
            System.err.println("Input file error: " + e.getMessage());
        }
    }

    private void executeCommand(SimulationCommand command) {
        switch (command) {
            case SimulationCommand.AddVehicle vehicle -> {
                try {
                    simulation.addVehicle(vehicle.vehicleId(), vehicle.startRoad(), vehicle.endRoad());
                } catch (IllegalVehicleDestination e) {
                    System.err.println("Simulation input error for vehicle " + vehicle.vehicleId() + ": " + e.getMessage());
                }
                notifyObserversOfNewVehicle(vehicle.vehicleId(), vehicle.startRoad(), vehicle.endRoad());
            }
            case SimulationCommand.Step ignored ->  {
                List<Vehicle> leftVehicles = simulation.step();
                notifyObserversOfNewStep(leftVehicles);
            }
        }
    }

    private void notifyObserversOfEndOfSimulation() {
        for (SimulationObserver observer : observers) {
            observer.endOfSimulation();
        }
    }

    private void notifyObserversOfNewStep(List<Vehicle> leftVehicles) {
        for (SimulationObserver observer : observers) {
            observer.newStep(leftVehicles, simulation.getIntersection());
        }
    }

    private void notifyObserversOfNewVehicle(String vehicleId, RoadsDirection startRoad, RoadsDirection endRoad) {
        for (SimulationObserver observer : observers) {
            observer.newVehicle(vehicleId, startRoad, endRoad);
        }
    }

    public void registerObserver(SimulationObserver observer) {
        observers.add(observer);
    }
}
