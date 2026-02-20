package pl.bartol0220.stls.exceptions;

public class MaxPhaseTimeLimit extends Exception {
    public MaxPhaseTimeLimit(int minLimit, int maxLimit) {
        super("Max phase time must be between " + minLimit + " and " + maxLimit + ".");
    }
}
