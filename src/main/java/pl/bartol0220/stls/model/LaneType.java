package pl.bartol0220.stls.model;

public enum LaneType {
    STRAIGHT,
    RIGHT,
    LEFT,
    U_TURN;

    public String toString() {
        return switch (this) {
            case STRAIGHT -> "↑";
            case RIGHT -> "↱";
            case LEFT -> "↰";
            case U_TURN -> "↶";
        };
    }
}
