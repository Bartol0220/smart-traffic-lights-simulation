package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;

import java.util.List;

public abstract class AbstractLightController {
    protected final Intersection intersection;
    protected final List<LightPhase> lightPhases;
    protected int currentPhaseIndex = 0;
    protected int currentPhaseTime = 0;

    public AbstractLightController(Intersection intersection) throws EmptyLightPhases {
        this.intersection = intersection;
        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        lightPhases = lightPhaseGenerator.GenerateLightPhases(intersection);
        if  (lightPhases.isEmpty()) {
            throw new EmptyLightPhases();
        }
    }

    protected void nextPhase() {
        currentPhaseTime = 0;
        currentPhaseIndex = (currentPhaseIndex + 1)  % lightPhases.size();
    }

    public abstract void initLightController();

    public abstract void step();
}
