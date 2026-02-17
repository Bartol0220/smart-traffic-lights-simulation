package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.TrafficLane;

import java.util.*;

public class VehiclesPriorityLightController extends AdvancedLightController {
    public VehiclesPriorityLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
    }

    public VehiclesPriorityLightController(Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        super(intersection, maxPhaseTime);
    }

    @Override
    protected int calculatePriority(TrafficLane lane) {
        return (int) Math.round(lane.getVehiclesPriority() * Math.max(waitingTimes.get(lane) * 0.5, 1));
    }
}
