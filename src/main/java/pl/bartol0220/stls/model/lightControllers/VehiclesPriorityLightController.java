package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.TrafficLane;

import java.util.*;

public class VehiclesPriorityLightController extends AbstractLightController {
    private final Map<TrafficLane, Integer> waitingTimes = new HashMap<>();
    private final Map<TrafficLane, Integer> currentPriorities = new HashMap<>();
    private final Map<TrafficLane, List<LightPhase>> phasesWithTrafficLane = new HashMap<>();
    private final Map<LightPhase, Integer> lightPhasesIndex = new HashMap<>();
    private final int maxPhaseTime;

    private void prepareDataStructures(Intersection intersection) {
        for (TrafficLane lane : intersection.getAllLanes()) {
            waitingTimes.put(lane, 0);
            phasesWithTrafficLane.put(lane, new ArrayList<>());
        }

        for (int i = 0; i < lightPhases.lightPhases().size(); i++) {
            LightPhase lightPhase =  lightPhases.lightPhases().get(i);
            lightPhasesIndex.put(lightPhase, i);

            for (TrafficLane lane : lightPhase.getTrafficLanes()) {
                phasesWithTrafficLane.get(lane).add(lightPhase);
            }
        }
    }

    public VehiclesPriorityLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
        this.maxPhaseTime = 5;
        prepareDataStructures(intersection);
    }

    public VehiclesPriorityLightController(Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        super(intersection);
        this.maxPhaseTime = maxPhaseTime;
        prepareDataStructures(intersection);
    }

    @Override
    public void initLightController() {
        calculatePriorities();
        TrafficLane bestTrafficLane = findBestTrafficLane();
        LightPhase bestLightPhase = findBestLightPhase(bestTrafficLane);

        currentPhaseIndex = lightPhasesIndex.get(bestLightPhase);
        lightPhases.lightPhases().get(currentPhaseIndex).changeLightToGreen();
    }

    @Override
    public void step() {
        calculatePriorities();
        TrafficLane bestTrafficLane = findBestTrafficLane();
        LightPhase bestLightPhase = findBestLightPhase(bestTrafficLane);

        if (currentPhaseIndex != lightPhasesIndex.get(bestLightPhase)) {
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToRed();
            currentPhaseIndex = lightPhasesIndex.get(bestLightPhase);
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToGreen();
        }

        changeWaitingTimes();
    }

    private int calculatePriority(TrafficLane lane) {
        return (int) Math.round(lane.getVehiclesPriority() * Math.max(waitingTimes.get(lane) * 0.5, 1));
    }

    private void calculatePriorities() {
        int priority;
        for (TrafficLane lane : intersection.getAllLanes()) {
            if (waitingTimes.get(lane) >= maxPhaseTime) {
                priority = Integer.MAX_VALUE;
            } else {
                priority = calculatePriority(lane);
            }
            currentPriorities.put(lane, priority);
        }
    }

    private TrafficLane findBestTrafficLane() {
        Optional<Map.Entry<TrafficLane, Integer>> maxEntry = currentPriorities.entrySet().stream().max(Map.Entry.comparingByValue());
        return maxEntry.map(Map.Entry::getKey).orElse(currentPriorities.keySet().iterator().next());
    }

    private LightPhase findBestLightPhase(TrafficLane bestTrafficLane) {
        LightPhase bestLightPhase = phasesWithTrafficLane.get(bestTrafficLane).getFirst();
        int bestLightPhasePriority = Integer.MIN_VALUE;

        for (LightPhase lightPhase : phasesWithTrafficLane.get(bestTrafficLane)) {
            int lightPhasePriority = 0;

            for (TrafficLane lane : lightPhase.getTrafficLanes()) {
                lightPhasePriority += currentPriorities.get(lane);
            }

            if  (lightPhasePriority > bestLightPhasePriority) {
                bestLightPhasePriority = lightPhasePriority;
                bestLightPhase = lightPhase;
            }
        }

        return bestLightPhase;
    }

    private void changeWaitingTimes() {
        Set<TrafficLane> trafficLanesSet = lightPhases.lightPhases().get(currentPhaseIndex).getTrafficLanes();

        for (TrafficLane lane : intersection.getAllLanes()) {
            if (trafficLanesSet.contains(lane)) {
                waitingTimes.put(lane, 0);
            } else {
                waitingTimes.put(lane, waitingTimes.get(lane) + 1);
            }
        }
    }
}
