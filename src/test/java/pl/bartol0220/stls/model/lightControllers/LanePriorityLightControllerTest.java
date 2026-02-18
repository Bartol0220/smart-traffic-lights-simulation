package pl.bartol0220.stls.model.lightControllers;

import org.junit.jupiter.api.Test;
import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Car;
import pl.bartol0220.stls.model.vehicles.Vehicle;
import pl.bartol0220.stls.simulation.Simulation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LanePriorityLightControllerTest {
    @Test
    void lanePriorityLightControllerWorks() throws EmptyLightPhases, InvalidTrafficLaneDirectionException, IllegalVehicleDestination {
        Intersection intersection = new Intersection();

        Road northRoad = intersection.getRoad(RoadsDirection.NORTH);
        northRoad.addTrafficLane(List.of(RoadsDirection.SOUTH), 5);

        Road eastRoad = intersection.getRoad(RoadsDirection.EAST);
        eastRoad.addTrafficLane(List.of(RoadsDirection.WEST));

        Road southRoad = intersection.getRoad(RoadsDirection.SOUTH);
        southRoad.addTrafficLane(List.of(RoadsDirection.NORTH), 5);

        Road westRoad = intersection.getRoad(RoadsDirection.WEST);
        westRoad.addTrafficLane(List.of(RoadsDirection.EAST));


        AbstractLightController lightController = new LanePriorityLightController(intersection);
        lightController.initLightController();

        Simulation simulation = new Simulation(intersection, lightController);

        Vehicle vehicle = new Car("1", RoadsDirection.NORTH, RoadsDirection.SOUTH);
        simulation.addVehicle(vehicle);

        vehicle = new Car("2", RoadsDirection.SOUTH, RoadsDirection.NORTH);
        simulation.addVehicle(vehicle);

        vehicle = new Car("3", RoadsDirection.EAST, RoadsDirection.WEST);
        simulation.addVehicle(vehicle);
        vehicle = new Car("4", RoadsDirection.EAST, RoadsDirection.WEST);
        simulation.addVehicle(vehicle);

        vehicle = new Car("5", RoadsDirection.WEST, RoadsDirection.EAST);
        simulation.addVehicle(vehicle);
        vehicle = new Car("6", RoadsDirection.WEST, RoadsDirection.EAST);
        simulation.addVehicle(vehicle);

        List<Vehicle> leftVehicles = simulation.step();

        assertEquals(2, leftVehicles.size());
        assertEquals(2, intersection.getRoad(RoadsDirection.WEST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(2, intersection.getRoad(RoadsDirection.EAST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.NORTH).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.SOUTH).getTrafficLanes().getFirst().getVehiclesPriority());

        vehicle = new Car("7", RoadsDirection.NORTH, RoadsDirection.SOUTH);
        simulation.addVehicle(vehicle);

        vehicle = new Car("8", RoadsDirection.SOUTH, RoadsDirection.NORTH);
        simulation.addVehicle(vehicle);

        leftVehicles = simulation.step();

        assertEquals(2, leftVehicles.size());
        assertEquals(2, intersection.getRoad(RoadsDirection.WEST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(2, intersection.getRoad(RoadsDirection.EAST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.NORTH).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.SOUTH).getTrafficLanes().getFirst().getVehiclesPriority());

        leftVehicles = simulation.step();

        assertEquals(2, leftVehicles.size());
        assertEquals(1, intersection.getRoad(RoadsDirection.WEST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(1, intersection.getRoad(RoadsDirection.EAST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.NORTH).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.SOUTH).getTrafficLanes().getFirst().getVehiclesPriority());

        leftVehicles = simulation.step();

        assertEquals(2, leftVehicles.size());
        assertEquals(0, intersection.getRoad(RoadsDirection.WEST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.EAST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.NORTH).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.SOUTH).getTrafficLanes().getFirst().getVehiclesPriority());

        leftVehicles = simulation.step();

        assertEquals(0, leftVehicles.size());
        assertEquals(0, intersection.getRoad(RoadsDirection.WEST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.EAST).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.NORTH).getTrafficLanes().getFirst().getVehiclesPriority());
        assertEquals(0, intersection.getRoad(RoadsDirection.SOUTH).getTrafficLanes().getFirst().getVehiclesPriority());
    }
}