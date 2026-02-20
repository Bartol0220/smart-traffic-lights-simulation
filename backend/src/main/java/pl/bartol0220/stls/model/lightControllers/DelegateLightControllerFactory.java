package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.util.DelegateLightControllerType;

public class DelegateLightControllerFactory {
    public AbstractDelegateLightController create(DelegateLightControllerType type, Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        return switch (type) {
            case TIME -> new TimeLightController(intersection, maxPhaseTime);
            case VEHICLES_PRIORITY -> new VehiclesPriorityLightController(intersection, maxPhaseTime);
            case LANE_PRIORITY -> new LanePriorityLightController(intersection, maxPhaseTime);
        };
    }
}
