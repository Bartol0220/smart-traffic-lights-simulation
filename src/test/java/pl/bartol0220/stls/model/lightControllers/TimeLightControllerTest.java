package pl.bartol0220.stls.model.lightControllers;

import org.junit.jupiter.api.Test;
import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.TrafficLane;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimeLightControllerTest {

    @Test
    void timeLightControllerWorks0() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.EAST));

        try {
            AbstractLightController lightController = new TimeLightController(intersection, 1);

            lightController.initLightController();

            LightPhase firstLightPhase = lightController.lightPhases.lightPhases().getFirst();
            LightPhase secondLightPhase = lightController.lightPhases.lightPhases().getLast();

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }

            lightController.step();

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            lightController.step();

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
        } catch (EmptyLightPhases e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void timeLightControllerWorks1() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.EAST, RoadsDirection.SOUTH, RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.WEST, RoadsDirection.NORTH, RoadsDirection.EAST));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST, RoadsDirection.NORTH));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH));

        try {
            AbstractLightController lightController = new TimeLightController(intersection, 2);

            lightController.initLightController();

            LightPhase firstLightPhase = lightController.lightPhases.lightPhases().getFirst();
            LightPhase secondLightPhase = lightController.lightPhases.lightPhases().get(1);
            LightPhase thirdLightPhase = lightController.lightPhases.lightPhases().get(2);
            LightPhase fourthLightPhase = lightController.lightPhases.lightPhases().get(3);

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : thirdLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : fourthLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }

            lightController.step();
            lightController.step();

            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : thirdLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : fourthLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }

            lightController.step();
            lightController.step();

            for (TrafficLane trafficLane : thirdLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : fourthLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }

            lightController.step();
            lightController.step();

            for (TrafficLane trafficLane : fourthLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : thirdLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }

            lightController.step();
            lightController.step();

            for (TrafficLane trafficLane : firstLightPhase.getTrafficLanes()) {
                assertTrue(trafficLane.getTrafficLight().canVehiclePass());
            }

            for (TrafficLane trafficLane : secondLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : thirdLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }
            for (TrafficLane trafficLane : fourthLightPhase.getTrafficLanes()) {
                assertFalse(trafficLane.getTrafficLight().canVehiclePass());
            }

        } catch (EmptyLightPhases e) {
            System.err.println(e.getMessage());
        }
    }
}