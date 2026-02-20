package pl.bartol0220.stls.exceptions;

import pl.bartol0220.stls.model.util.RoadsDirection;

public class NoLanesForRoad extends Exception {
    public NoLanesForRoad(RoadsDirection direction) {
        super("Road " + direction + " must have at least one lane.");
    }
}
