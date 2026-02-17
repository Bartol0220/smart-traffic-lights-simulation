package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;

public class TimeLightController extends AbstractLightController {
    private final int maxPhaseTime;
    private int currentPhaseTime = 0;

    public TimeLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
        maxPhaseTime = 3;
    }

    public TimeLightController(Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        super(intersection);
        this.maxPhaseTime = maxPhaseTime;
    }

    private void nextPhase() {
        currentPhaseTime = 0;
        currentPhaseIndex = (currentPhaseIndex + 1)  % lightPhases.lightPhases().size();
    }

    @Override
    public void initLightController() {
        lightPhases.lightPhases().get(currentPhaseTime).changeLightToGreen();
    }

    @Override
    public void step() {
        currentPhaseTime += 1;
        if (currentPhaseTime >= maxPhaseTime) {
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToRed();
            nextPhase();
            lightPhases.lightPhases().get(currentPhaseIndex).changeLightToGreen();
        }
    }
}
