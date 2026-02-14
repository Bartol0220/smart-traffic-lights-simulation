package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.model.Intersection;

public abstract class AbstractLightController {
    private final Intersection intersection;

    protected AbstractLightController(Intersection intersection) {
        this.intersection = intersection;
    }
}
