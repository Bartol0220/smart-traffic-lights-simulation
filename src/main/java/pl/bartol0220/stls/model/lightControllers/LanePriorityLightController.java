package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.TrafficLane;

public class LanePriorityLightController extends AdvancedLightController {
    public LanePriorityLightController(Intersection intersection) throws EmptyLightPhases {
        super(intersection);
    }

    public LanePriorityLightController(Intersection intersection, int maxPhaseTime) throws EmptyLightPhases {
        super(intersection, maxPhaseTime);
    }

    @Override
    protected int calculatePriority(TrafficLane lane) {
        if (lane.getNumberOfVehicles() == 0) {
            return lane.getVehiclesPriority();
        }
        return (int) Math.round((lane.getVehiclesPriority() + lane.getLanePriority()) * Math.max(waitingTimes.get(lane) * 0.5, 1));
    }
}
