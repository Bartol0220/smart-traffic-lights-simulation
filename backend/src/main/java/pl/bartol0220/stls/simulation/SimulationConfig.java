package pl.bartol0220.stls.simulation;

import pl.bartol0220.stls.exceptions.*;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.lightControllers.*;
import pl.bartol0220.stls.model.util.DelegateLightControllerType;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.*;

public class SimulationConfig {
    public static int MAX_PHASE_TIME_MIN_VALUE = 1;
    public static int MAX_PHASE_TIME_MAX_VALUE = 10;
    public static int MAX_NUMBER_OF_LANES = 3;
    public static int MAX_LANE_PRIORITY = 10;
    public static int MIN_LANE_PRIORITY = 1;
    private final Intersection intersection;
    private boolean isEmergencyLightController = false;
    private int maxPhaseTime = 5;
    private DelegateLightControllerType type = DelegateLightControllerType.LANE_PRIORITY;

    public SimulationConfig(Intersection intersection) {
        this.intersection = intersection;
    }

    public SimulationConfig() {
        intersection = new Intersection();
    }

    public void addLane(RoadsDirection entryDirection, List<RoadsDirection> exitDirections, int lanePriority) throws InvalidTrafficLaneDirectionException, MaxNumberOfLanesException, LanePriorityLimit {
        if (lanePriority > MAX_LANE_PRIORITY || lanePriority < MIN_LANE_PRIORITY) throw new LanePriorityLimit(MIN_LANE_PRIORITY, MAX_LANE_PRIORITY);
        Road road = intersection.getRoad(entryDirection);
        if (road.getTrafficLanes().size() >= MAX_NUMBER_OF_LANES) {
            throw new MaxNumberOfLanesException(entryDirection);
        }
        road.addTrafficLane(exitDirections, lanePriority);
    }

    public void removeLane(RoadsDirection entryDirection, int index) {
        Road road = intersection.getRoad(entryDirection);
        road.removeTrafficLane(index);
    }

    public void setMaxPhaseTime(int maxPhaseTime) throws MaxPhaseTimeLimit {
        if (maxPhaseTime < MAX_PHASE_TIME_MIN_VALUE || maxPhaseTime > MAX_PHASE_TIME_MAX_VALUE) throw new MaxPhaseTimeLimit(MAX_PHASE_TIME_MIN_VALUE, MAX_PHASE_TIME_MAX_VALUE);
        this.maxPhaseTime = maxPhaseTime;
    }

    public void setControllerType(DelegateLightControllerType type) {
        this.type = type;
    }

    public void setEmergencyLightController(boolean val) {
        isEmergencyLightController = val;
    }

    public Simulation makeSimulation() throws EmptyLightPhases {
        AbstractDelegateLightController delegate = new DelegateLightControllerFactory().create(type, intersection, maxPhaseTime);
        AbstractLightController lightController = delegate;
        if (isEmergencyLightController) {
            lightController = new EmergencyVehiclesLightController(delegate);
        }
        return new Simulation(intersection, lightController);
    }

    public void addLaneForEachRoad() throws InvalidTrafficLaneDirectionException {
        Road nortRoad = intersection.getRoad(RoadsDirection.NORTH);
        nortRoad.addTrafficLane(List.of(RoadsDirection.SOUTH));

        Road southRoad = intersection.getRoad(RoadsDirection.SOUTH);
        southRoad.addTrafficLane(List.of(RoadsDirection.NORTH));

        Road eastRoad = intersection.getRoad(RoadsDirection.EAST);
        eastRoad.addTrafficLane(List.of(RoadsDirection.WEST));

        Road westRoad = intersection.getRoad(RoadsDirection.WEST);
        westRoad.addTrafficLane(List.of(RoadsDirection.EAST));
    }

    public void prepareDefaultSimulation() throws InvalidTrafficLaneDirectionException, MaxPhaseTimeLimit {
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

        setControllerType(DelegateLightControllerType.LANE_PRIORITY);
        setMaxPhaseTime(5);
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public boolean getIsEmergencyLightController() {
        return isEmergencyLightController;
    }

    public int getMaxPhaseTime() {
        return maxPhaseTime;
    }

    public DelegateLightControllerType getTpe() {
        return type;
    }
}
