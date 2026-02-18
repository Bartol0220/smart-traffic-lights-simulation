package pl.bartol0220.stls.model.util;

public enum LaneType {
    STRAIGHT,
    RIGHT,
    LEFT,
    U_TURN;

    public String toString() {
        return switch (this) {
            case STRAIGHT -> "S";
            case RIGHT -> "R";
            case LEFT -> "L";
            case U_TURN -> "U";
        };
    }
}
