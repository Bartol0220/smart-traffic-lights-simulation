package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.TrafficLane;

import java.util.*;

public abstract class AdvancedLightController extends AbstractLightController {
    protected final Map<TrafficLane, Integer> waitingTimes = new HashMap<>();
    protected final Map<TrafficLane, Integer> currentPriorities = new HashMap<>();
    protected final Map<TrafficLane, List<LightPhase>> phasesWithTrafficLane = new HashMap<>();
    protected final Map<LightPhase, Integer> lightPhasesIndex = new HashMap<>();
    protected final int maxPhaseTime;

    protected void prepareDataStructures() {
        for (TrafficLane lane : intersection.getAllLanes()) {
            waitingTimes.put(lane, 0);
            phasesWithTrafficLane.put(lane, new ArrayList<>());
        }

        indexLightPhases(lightPhases.lightPhases(), lightPhasesIndex, phasesWithTrafficLane);
    }

    public AdvancedLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
        this.maxPhaseTime = 5;
        prepareDataStructures();
    }

    public AdvancedLightController(Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        super(intersection);
        this.maxPhaseTime = maxPhaseTime;
        prepareDataStructures();
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


    protected abstract int calculatePriority(TrafficLane lane);

    protected void calculatePriorities() {
        int priority;
        for (TrafficLane lane : intersection.getAllLanes()) {
            if (waitingTimes.get(lane) >= maxPhaseTime && lane.getNumberOfVehicles() != 0) {
                priority = Integer.MAX_VALUE;
            } else {
                priority = calculatePriority(lane);
            }
            currentPriorities.put(lane, priority);
        }
    }

    protected TrafficLane findBestTrafficLane() {
        Optional<Map.Entry<TrafficLane, Integer>> maxEntry = currentPriorities.entrySet().stream().max(Map.Entry.comparingByValue());
        return maxEntry.map(Map.Entry::getKey).orElse(currentPriorities.keySet().iterator().next());
    }

    protected LightPhase findBestLightPhase(TrafficLane bestTrafficLane) {
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

    protected void changeWaitingTimes() {
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
