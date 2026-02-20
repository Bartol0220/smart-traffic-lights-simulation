package pl.bartol0220.stls.model.util;

import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.simulation.SimulationConfig;

public enum IntersectionType {
    SIMPLE_INTERSECTION,
    INTERSECTION_WITH_MAIN_ROAD,
    LARGE_INTERSECTION;

    @Override
    public String toString() {
        return switch (this) {
            case SIMPLE_INTERSECTION -> "Simple intersection";
            case INTERSECTION_WITH_MAIN_ROAD -> "Intersection with main road";
            case LARGE_INTERSECTION -> "Large intersection";
        };
    }

    public void prepareIntersection(SimulationConfig simConfig) throws InvalidTrafficLaneDirectionException {
        switch (this) {
            case SIMPLE_INTERSECTION -> simConfig.prepareSimpleIntersection();
            case INTERSECTION_WITH_MAIN_ROAD -> simConfig.prepareIntersectionWithMainRoad();
            case LARGE_INTERSECTION -> simConfig.prepareLargeIntersection();
        }
    }
}
