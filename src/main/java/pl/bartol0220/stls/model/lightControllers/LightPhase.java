package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.model.TrafficLane;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LightPhase {
    private final Set<TrafficLane> trafficLanes = new HashSet<TrafficLane>();

    public void addTrafficLane(TrafficLane trafficLane) {
        trafficLanes.add(trafficLane);
    }

    public Set<TrafficLane> getTrafficLanes() {
        return Collections.unmodifiableSet(trafficLanes);
    }

    public void changeLightToGreen() {
        for (TrafficLane trafficLane : trafficLanes) {
            trafficLane.changeLightToGreen();
        }
    }

    public void changeLightToRed() {
        for (TrafficLane trafficLane : trafficLanes) {
            trafficLane.changeLightToRed();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Light phase:\n");
        for (TrafficLane trafficLane : trafficLanes) {
            sb.append("  ")
                    .append(trafficLane.getEntryDirection())
                    .append(" -> ")
                    .append(trafficLane.getExitDirections())
                    .append(" | ")
                    .append(trafficLane.getLaneType())
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LightPhase that)) return false;
        return Objects.equals(trafficLanes, that.trafficLanes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trafficLanes);
    }
}
