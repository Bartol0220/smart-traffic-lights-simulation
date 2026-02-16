package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;


public abstract class AbstractLightController {
    protected final Intersection intersection;
    protected final LightPhaseSequence lightPhases;
    protected int currentPhaseIndex = 0;
    protected int currentPhaseTime = 0;

    public AbstractLightController(Intersection intersection) throws EmptyLightPhases {
        this.intersection = intersection;
        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        lightPhases = lightPhaseGenerator.generateLightPhases(intersection);
        if  (lightPhases.lightPhases().isEmpty()) {
            throw new EmptyLightPhases();
        }
    }

    protected void nextPhase() {
        currentPhaseTime = 0;
        currentPhaseIndex = (currentPhaseIndex + 1)  % lightPhases.lightPhases().size();
    }

    public abstract void initLightController();

    public abstract void step();

    public LightPhaseSequence getLightPhaseSequence() {
        return lightPhases;
    }
}
