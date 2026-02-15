package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;

public class TimeLightController extends AbstractLightController {
    private final int maxPhaseTime;

    public TimeLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
        maxPhaseTime = 3;
    }

    public TimeLightController(Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        super(intersection);
        this.maxPhaseTime = maxPhaseTime;
    }

    @Override
    public void initLightController() {
        lightPhases.get(currentPhaseTime).changeLightToGreen();
    }

    @Override
    public void step() {
        currentPhaseTime += 1;
        if (currentPhaseTime >= maxPhaseTime) {
            lightPhases.get(currentPhaseIndex).changeLightToRed();
            nextPhase();
            lightPhases.get(currentPhaseIndex).changeLightToGreen();
        }
    }
}
