package pl.bartol0220.stls.exceptions;

public class LanePriorityLimit extends Exception {
    public LanePriorityLimit(int minLimit, int maxLimit) {
        super("Traffic lane priority must be between " + minLimit + " and " + maxLimit + ".");
    }
}
