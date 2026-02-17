package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;


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
}
