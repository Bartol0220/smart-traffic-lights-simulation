package pl.bartol0220.stls.model.vehicles;

import pl.bartol0220.stls.model.RoadsDirection;

public abstract class Vehicle {
    private final String id;
    private final RoadsDirection startRoad;
    private final RoadsDirection endRoad;
    private final int priority;

    public Vehicle(String id, RoadsDirection startRoad, RoadsDirection endRoad, int priority) {
        this.id = id;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public RoadsDirection getStartRoad() {
        return startRoad;
    }

    public RoadsDirection getEndRoad() {
        return endRoad;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return id;
    }
}
