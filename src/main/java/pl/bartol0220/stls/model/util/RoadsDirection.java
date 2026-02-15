package pl.bartol0220.stls.model.util;

public enum RoadsDirection {
    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3);

    private final int index;

    RoadsDirection(int index) {
        this.index = index;
    }

    public String toString(){
        return switch (this) {
            case NORTH -> "N";
            case EAST -> "E";
            case SOUTH -> "S";
            case WEST -> "W";
        };
    }

    public LaneType getLaneType(RoadsDirection entryDirection) {
        int diff = (entryDirection.index - this.index + 4) % 4;
        return switch (diff) {
            case 1 -> LaneType.RIGHT;
            case 2 -> LaneType.STRAIGHT;
            case 3 -> LaneType.LEFT;
            default -> LaneType.U_TURN;
        };
    }

    public boolean isOpposite(RoadsDirection other) {
        return Math.abs(other.index - this.index) == 2;
    }

    public boolean isNextClockwise(RoadsDirection other) {
        return (this.index + 1) % 4 == other.index;
    }
}
