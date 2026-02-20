package pl.bartol0220.stls.simulation;

import pl.bartol0220.stls.dto.*;
import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.TrafficLane;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.util.VehicleType;
import pl.bartol0220.stls.model.vehicles.Vehicle;
import pl.bartol0220.stls.model.vehicles.VehicleFactory;

import java.util.ArrayList;
import java.util.List;

public class WebAppSimulationRunner {
    private final Simulation simulation;
    private final VehicleFactory vehicleFactory = new VehicleFactory();
    private int stepCounter = 0;

    public WebAppSimulationRunner(Simulation simulation) {
        this.simulation = simulation;
    }

    private SimulationDto buildSimulationDto() {
        return buildSimulationDto(List.of());
    }

    private SimulationDto buildSimulationDto(List<Vehicle> leftVehicles) {
        List<VehicleDto> leftVehiclesDto = leftVehicles.stream()
                .map(v -> new VehicleDto(
                        v.getId(),
                        v.getStartRoad(),
                        v.getEndRoad(),
                        v.getVehicleType()
                ))
                .toList();

        Intersection intersection = simulation.getIntersection();
        List<RoadDto> roads = new ArrayList<>();

        for (RoadsDirection roadsDirection : RoadsDirection.values()) {
            Road road = intersection.getRoad(roadsDirection);
            roads.add(new RoadDto(roadsDirection, getTrafficLaneDtos(road)));
        }

        IntersectionDto intersectionDto = new IntersectionDto(roads);
        return new SimulationDto(intersectionDto, stepCounter, leftVehiclesDto);
    }

    public static List<TrafficLaneDto> getTrafficLaneDtos(Road road) {
        List<TrafficLaneDto> trafficLanes = new ArrayList<>();

        for (TrafficLane lane : road.getTrafficLanes()) {
            List<VehicleDto> vehicles = lane.getVehicles().stream()
                    .map(v -> new VehicleDto(
                            v.getId(),
                            v.getStartRoad(),
                            v.getEndRoad(),
                            v.getVehicleType()
                    ))
                    .toList();

            trafficLanes.add(new TrafficLaneDto(
                    lane.getIndex(),
                    lane.getLanePriority(),
                    lane.getEntryDirection(),
                    lane.getExitDirections(),
                    lane.getLaneType(),
                    vehicles,
                    new TrafficLightDto(lane.getTrafficLight().getColor())
            ));
        }
        return trafficLanes;
    }

    public SimulationDto step() {
        stepCounter++;
        List<Vehicle> leftVehicles = simulation.step();
        return buildSimulationDto(leftVehicles);
    }

    public SimulationDto addVehicle(VehicleType vehicleType, String vehicleId, RoadsDirection startRoad, RoadsDirection endRoad) throws IllegalVehicleDestination {
        Vehicle vehicle = vehicleFactory.create(vehicleType, vehicleId, startRoad, endRoad);
        simulation.addVehicle(vehicle);
        return buildSimulationDto();
    }

    public SimulationDto getSimulationDto() {
        return buildSimulationDto();
    }
}
