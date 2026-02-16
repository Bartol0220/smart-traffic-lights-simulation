package pl.bartol0220.stls.simulation;

import org.junit.jupiter.api.Test;
import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.lightControllers.*;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Car;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    public void simulationWorks() throws EmptyLightPhases, InvalidTrafficLaneDirectionException {
        Intersection intersection = new Intersection(Map.of(
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

        AbstractLightController lightController = new TimeLightController(intersection);

        Simulation simulation = new Simulation(intersection, lightController);

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        List<Vehicle> leftVehicles = simulation.step();

        assertEquals(0, leftVehicles.size());

        List<Vehicle> expectedLeftVehicles = new LinkedList<>();

        lightPhases.lightPhases().getFirst().getTrafficLanes().forEach(trafficLane -> {
            RoadsDirection entryDirection = trafficLane.getEntryDirection();
            Set<RoadsDirection> exitDirections = trafficLane.getExitDirections();
            RoadsDirection exitDirection = exitDirections.iterator().next();

            try {
                Vehicle vehicle = new Car("car" + exitDirection, entryDirection, exitDirection);
                expectedLeftVehicles.add(vehicle);
                simulation.addVehicle(vehicle);
            } catch (IllegalVehicleDestination e) {
                throw new RuntimeException(e);
            }
        });

        leftVehicles = simulation.step();

        assertEquals(expectedLeftVehicles.size(), leftVehicles.size());
        assertTrue(expectedLeftVehicles.containsAll(leftVehicles));
        assertTrue(leftVehicles.containsAll(expectedLeftVehicles));

        leftVehicles = simulation.step();

        assertEquals(0, leftVehicles.size());
    }

    @Test
    public void simulationWorks2() throws EmptyLightPhases, InvalidTrafficLaneDirectionException {
        Intersection intersection = new Intersection(Map.of(
                RoadsDirection.NORTH, 4,
                RoadsDirection.SOUTH, 4,
                RoadsDirection.EAST, 1,
                RoadsDirection.WEST, 1
        ));

        Road nortRoad = intersection.getRoad(RoadsDirection.NORTH);
        nortRoad.addTrafficLane(List.of(RoadsDirection.EAST), 2);
        nortRoad.addTrafficLane(List.of(RoadsDirection.SOUTH));
        nortRoad.addTrafficLane(List.of(RoadsDirection.WEST), 2);

        Road southRoad = intersection.getRoad(RoadsDirection.SOUTH);
        southRoad.addTrafficLane(List.of(RoadsDirection.WEST), 2);
        southRoad.addTrafficLane(List.of(RoadsDirection.NORTH));
        southRoad.addTrafficLane(List.of(RoadsDirection.EAST), 2);

        Road eastRoad = intersection.getRoad(RoadsDirection.EAST);
        eastRoad.addTrafficLane(List.of(RoadsDirection.SOUTH));
        eastRoad.addTrafficLane(List.of(RoadsDirection.WEST, RoadsDirection.NORTH));

        Road westRoad = intersection.getRoad(RoadsDirection.WEST);
        westRoad.addTrafficLane(List.of(RoadsDirection.NORTH));
        westRoad.addTrafficLane(List.of(RoadsDirection.EAST, RoadsDirection.SOUTH));

        AbstractLightController lightController = new TimeLightController(intersection);

        Simulation simulation = new Simulation(intersection, lightController);

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        List<Vehicle> leftVehicles = simulation.step();

        assertEquals(0, leftVehicles.size());

        List<Vehicle> expectedLeftVehicles = new LinkedList<>();

        lightPhases.lightPhases().getFirst().getTrafficLanes().forEach(trafficLane -> {
            RoadsDirection entryDirection = trafficLane.getEntryDirection();
            Set<RoadsDirection> exitDirections = trafficLane.getExitDirections();
            RoadsDirection exitDirection = exitDirections.iterator().next();

            try {
                Vehicle vehicle = new Car("car" + exitDirection, entryDirection, exitDirection);
                expectedLeftVehicles.add(vehicle);
                simulation.addVehicle(vehicle);
            } catch (IllegalVehicleDestination e) {
                throw new RuntimeException(e);
            }
        });

        leftVehicles = simulation.step();

        assertEquals(expectedLeftVehicles.size(), leftVehicles.size());
        assertTrue(expectedLeftVehicles.containsAll(leftVehicles));
        assertTrue(leftVehicles.containsAll(expectedLeftVehicles));


        leftVehicles = simulation.step();

        assertEquals(0, leftVehicles.size());
    }
}