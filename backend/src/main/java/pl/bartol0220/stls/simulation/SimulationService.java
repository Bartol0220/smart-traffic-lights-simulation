package pl.bartol0220.stls.simulation;

import org.springframework.stereotype.Service;
import pl.bartol0220.stls.dto.*;
import pl.bartol0220.stls.exceptions.*;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pl.bartol0220.stls.simulation.WebAppSimulationRunner.getTrafficLaneDtos;

@Service
public class SimulationService {
    private WebAppSimulationRunner simulationRunner;
    private SimulationConfig simulationConfig = new SimulationConfig();

    public Object initSimulation() {
        try {
            for (RoadsDirection direction : RoadsDirection.values()) {
                if (simulationConfig.getIntersection().getRoad(direction).getTrafficLanes().isEmpty()) throw new NoLanesForRoad(direction);
            }

            simulationRunner = new WebAppSimulationRunner(simulationConfig.makeSimulation());
        } catch (EmptyLightPhases | NoLanesForRoad e) {
            return new ErrorDto(e.getMessage());
        }
        return simulationRunner.getSimulationDto();
    }

    public SimulationDto getCurrentState() {
        return simulationRunner.getSimulationDto();
    }

    public SimulationDto step() {
        return simulationRunner.step();
    }

    public Object addVehicle(VehicleDto vehicleDto) {
        try {
            return simulationRunner.addVehicle(vehicleDto.type(), vehicleDto.id(), vehicleDto.startRoad(), vehicleDto.endRoad());
        } catch (IllegalVehicleDestination e) {
            return new ErrorDto(e.getMessage());
        }
    }

    public SimulationConfigDto getSimulationConfig() {
        return buildSimulationConfigDto();
    }

    public Object newSimulationConfig(Map<RoadsDirection, Integer> priorities) {
        for (RoadsDirection direction : RoadsDirection.values()) {
            if (!priorities.containsKey(direction)) {
                priorities.put(direction, 1);
            } else if (priorities.get(direction) > SimulationConfig.MAX_LANE_PRIORITY || priorities.get(direction) < SimulationConfig.MIN_LANE_PRIORITY) {
                return new ErrorDto("Road priority must be between " + SimulationConfig.MIN_LANE_PRIORITY + " and " + SimulationConfig.MAX_LANE_PRIORITY + ".");
            }
        }

        Intersection intersection = new Intersection(priorities);
        simulationConfig = new SimulationConfig(intersection);
        try {
            simulationConfig.addLaneForEachRoad();
        } catch (InvalidTrafficLaneDirectionException e) {
            return new ErrorDto(e.getMessage());
        }
        return buildSimulationConfigDto();
    }

    public Object newSimulationConfig() {
        Intersection intersection = new Intersection();
        simulationConfig = new SimulationConfig(intersection);
        try {
            simulationConfig.addLaneForEachRoad();
        } catch (InvalidTrafficLaneDirectionException e) {
            return new ErrorDto(e.getMessage());
        }
        return buildSimulationConfigDto();
    }

    public Object newDefaultSimulation() {
        try {
            simulationConfig = new SimulationConfig();
            simulationConfig.prepareDefaultSimulation();
        } catch (InvalidTrafficLaneDirectionException | MaxPhaseTimeLimit e) {
            return new ErrorDto(e.getMessage());
        }
        return this.initSimulation();
    }

    public Object addLane(AddLaneDto addLaneDto) {
        try {
            if (addLaneDto.exitDirections().isEmpty()) throw new NoExitDirectionForLane();
            simulationConfig.addLane(addLaneDto.entryDirection(), addLaneDto.exitDirections(), addLaneDto.lanePriority());
        } catch (InvalidTrafficLaneDirectionException | NoExitDirectionForLane | MaxNumberOfLanesException | LanePriorityLimit e) {
            return new ErrorDto(e.getMessage());
        }
        return buildSimulationConfigDto();
    }

    public SimulationConfigDto removeLane(RoadsDirection entryDirection, int index) {
        simulationConfig.removeLane(entryDirection, index);
        return buildSimulationConfigDto();
    }

    public Object updateConfig(UpdateConfigDto updateConfigDto) {
        try {
            if (updateConfigDto.maxPhaseTime() != null) {
                simulationConfig.setMaxPhaseTime(updateConfigDto.maxPhaseTime());
            }

            if (updateConfigDto.controllerType() != null) {
                simulationConfig.setControllerType(updateConfigDto.controllerType());
            }

            if (updateConfigDto.emergencyLightController() != null) {
                simulationConfig.setEmergencyLightController(updateConfigDto.emergencyLightController());
            }
        } catch (MaxPhaseTimeLimit e) {
            return new ErrorDto(e.getMessage());
        }
        return buildSimulationConfigDto();
    }

    private SimulationConfigDto buildSimulationConfigDto() {
        Intersection intersection = simulationConfig.getIntersection();
        List<RoadDto> roads = new ArrayList<>();

        for (RoadsDirection roadsDirection : RoadsDirection.values()) {
            Road road = intersection.getRoad(roadsDirection);
            roads.add(new RoadDto(roadsDirection, getTrafficLaneDtos(road)));
        }

        IntersectionDto intersectionDto = new IntersectionDto(roads);
        return new SimulationConfigDto(
                intersectionDto,
                simulationConfig.getIsEmergencyLightController(),
                simulationConfig.getMaxPhaseTime(),
                simulationConfig.getTpe()
        );
    }
}
