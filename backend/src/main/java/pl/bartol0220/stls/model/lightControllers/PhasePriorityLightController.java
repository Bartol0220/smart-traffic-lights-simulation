package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.TrafficLane;
import pl.bartol0220.stls.model.lightControllers.phase.LightPhase;

import java.util.*;

public class PhasePriorityLightController extends AbstractDelegateLightController {
    private final Map<LightPhase, Integer> waitingTimes = new HashMap<>();
    private final Map<LightPhase, Integer> currentPriorities = new HashMap<>();
    private final Map<LightPhase, Integer> lightPhasesIndex = new HashMap<>();
    private final int maxPhaseTime;

    private void prepareDataStructures() {
        List<LightPhase> lightPhasesList = lightPhases.lightPhases();
        for (int i=0; i < lightPhasesList.size(); i++) {
            LightPhase phase = lightPhasesList.get(i);
            waitingTimes.put(phase, 0);
            lightPhasesIndex.put(phase, i);
        }
    }

    public PhasePriorityLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
        maxPhaseTime = 5;
        prepareDataStructures();
    }

    public PhasePriorityLightController(Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        super(intersection);
        this.maxPhaseTime = maxPhaseTime;
        prepareDataStructures();
    }

    @Override
    public void initLightController() {
        calculatePriorities();
        LightPhase bestLightPhase = findBestLightPhase();

        currentPhaseIndex = lightPhasesIndex.get(bestLightPhase);
        lightPhases.lightPhases().get(currentPhaseIndex).changeLightToGreen();
    }

    @Override
    public void step() {
        calculatePriorities();
        LightPhase bestLightPhase = findBestLightPhase();

        if (currentPhaseIndex != lightPhasesIndex.get(bestLightPhase)) {
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToRed();
            currentPhaseIndex = lightPhasesIndex.get(bestLightPhase);
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToGreen();
        }

        changeWaitingTimes();
    }

    private int calculatePriority(LightPhase phase) {
        int totalVehicles = 0;
        int totalPriority = 0;

        for (TrafficLane lane : phase.getTrafficLanes()) {
            totalVehicles += lane.getNumberOfVehicles();
            totalPriority += lane.getVehiclesPriority() + lane.getLanePriority();
        }

        return totalVehicles == 0 ? 0 : totalPriority;
    }

    private int countNumberOfVehiclesInLightPhase(LightPhase phase) {
        int res = 0;
        for (TrafficLane lane : phase.getTrafficLanes()) {
            res += lane.getNumberOfVehicles();
        }
        return res;
    }

    private void calculatePriorities() {
        int priority;

        for (LightPhase phase : lightPhases.lightPhases()) {
            if (waitingTimes.get(phase) >= maxPhaseTime && countNumberOfVehiclesInLightPhase(phase) != 0) {
                priority = Integer.MAX_VALUE;
            } else {
                priority = calculatePriority(phase);
            }
            currentPriorities.put(phase, priority);
        }
    }

    private LightPhase findBestLightPhase() {
        Optional<Map.Entry<LightPhase, Integer>> maxEntry = currentPriorities.entrySet().stream().max(Map.Entry.comparingByValue());
        return maxEntry.map(Map.Entry::getKey).orElse(currentPriorities.keySet().iterator().next());
    }

    private void changeWaitingTimes() {
        for (LightPhase phase : lightPhases.lightPhases()) {
            if (currentPhaseIndex == lightPhasesIndex.get(phase)) {
                waitingTimes.put(phase, 0);
            } else {
                waitingTimes.put(phase, waitingTimes.get(phase) + 1);
            }
        }
    }
}
