package pl.bartol0220.stls.exceptions;

public class NoExitDirectionForLane extends Exception {
    public NoExitDirectionForLane() {
        super("Lane must have at least one exit direction.");
    }
}
