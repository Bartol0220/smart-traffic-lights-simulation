package pl.bartol0220.stls.exceptions;

import pl.bartol0220.stls.model.util.RoadsDirection;

public class MaxNumberOfLanesException extends Exception {
    public MaxNumberOfLanesException(RoadsDirection direction) {
        super("Road " + direction + " already have max number of traffic lanes.");
    }
}
