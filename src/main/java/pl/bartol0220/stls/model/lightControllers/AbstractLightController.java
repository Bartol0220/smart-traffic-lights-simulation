package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.TrafficLane;

import java.util.List;
import java.util.Map;


public abstract class AbstractLightController {
    protected final Intersection intersection;
    protected final LightPhaseSequence lightPhases;
    protected int currentPhaseIndex = 0;

    public AbstractLightController(Intersection intersection) throws EmptyLightPhases {
        this.intersection = intersection;
        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        lightPhases = lightPhaseGenerator.generateLightPhases(intersection);
        if  (lightPhases.lightPhases().isEmpty()) {
            throw new EmptyLightPhases();
        }
    }

    public abstract void initLightController();

    public abstract void step();

    public LightPhaseSequence getLightPhaseSequence() {
        return lightPhases;
    }

    static void indexLightPhases(List<LightPhase> lightPhases, Map<LightPhase, Integer> lightPhasesIndex, Map<TrafficLane, List<LightPhase>> phasesWithTrafficLane) {
        for (int i = 0; i < lightPhases.size(); i++) {
            LightPhase lightPhase =  lightPhases.get(i);
            lightPhasesIndex.put(lightPhase, i);

            for (TrafficLane lane : lightPhase.getTrafficLanes()) {
                phasesWithTrafficLane.get(lane).add(lightPhase);
            }
        }
    }

    public int getCurrentPhaseIndex() {
        return currentPhaseIndex;
    }

    public void setCurrentPhaseIndex(int currentPhaseIndex) {
        this.currentPhaseIndex = currentPhaseIndex;
    }
}
