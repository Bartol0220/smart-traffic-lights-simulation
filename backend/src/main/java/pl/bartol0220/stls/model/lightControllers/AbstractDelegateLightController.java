package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;

public abstract class AbstractDelegateLightController extends AbstractLightController {
    public AbstractDelegateLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
    }
}
