package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.TrafficLane;

import java.util.*;

public class EmergencyVehiclesLightController extends AbstractLightController {
    private final AbstractLightController delegate;
    private final List<TrafficLane> emergencyLines = new ArrayList<>();
    private final Map<TrafficLane, List<LightPhase>> phasesWithTrafficLane = new HashMap<>();
    private final Map<LightPhase, Integer> lightPhasesIndex = new HashMap<>();

    private void prepareDataStructures() {
        for (TrafficLane lane : intersection.getAllLanes()) {
            phasesWithTrafficLane.put(lane, new ArrayList<>());
        }

        indexLightPhases(lightPhases.lightPhases(), lightPhasesIndex, phasesWithTrafficLane);
    }

    public EmergencyVehiclesLightController(AbstractLightController delegate) throws EmptyLightPhases {
        super(delegate.intersection);
        this.delegate = delegate;
        prepareDataStructures();
    }

    @Override
    public void initLightController() {
        delegate.initLightController();
    }

    @Override
    public void step() {
        emergencyLines.clear();
        for (TrafficLane lane : intersection.getAllLanes()) {
            if (lane.containsEmergencyVehicle()) {
                emergencyLines.add(lane);
            }
        }

        if (emergencyLines.isEmpty()) {
            delegate.step();
        } else {
            emergencyVehicleOnIntersection();
        }
    }

    private void emergencyVehicleOnIntersection() {
        Collections.shuffle(emergencyLines);
        TrafficLane emergencyLine = emergencyLines.getFirst();

        LightPhase bestLightPhase = phasesWithTrafficLane.get(emergencyLine).getFirst();
        int bestNumOfEmergency = Integer.MIN_VALUE;

        for (LightPhase lightPhase : phasesWithTrafficLane.get(emergencyLine)) {
            int numOfEmergency = 0;

            for (TrafficLane lane : lightPhase.getTrafficLanes()) {
                numOfEmergency += lane.containsEmergencyVehicle() ? 1 : 0;
            }

            if  (numOfEmergency > bestNumOfEmergency) {
                bestNumOfEmergency = numOfEmergency;
                bestLightPhase = lightPhase;
            }
        }

        int newPhaseIndex = lightPhasesIndex.get(bestLightPhase);

        if (currentPhaseIndex != newPhaseIndex) {
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToRed();
            currentPhaseIndex = newPhaseIndex;
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToGreen();
        }
    }
}
