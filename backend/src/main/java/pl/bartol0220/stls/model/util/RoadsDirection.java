package pl.bartol0220.stls.model.util;

import java.util.ArrayList;
import java.util.List;

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
            case NORTH -> "North";
            case EAST -> "East";
            case SOUTH -> "South";
            case WEST -> "West";
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

    public RoadsDirection getOpposite() {
        return RoadsDirection.values()[(this.index + 2) % 4];
    }

    public List<RoadsDirection> applyTurnTo( RoadsDirection seconLaneEntry, List<RoadsDirection> exitDirections) {
        List<RoadsDirection> result = new ArrayList<>();

        for (RoadsDirection direction : exitDirections) {
            int diff = (direction.index - this.index + 4) % 4;
            RoadsDirection newDirection = RoadsDirection.values()[(seconLaneEntry.index + diff) % 4];
            result.add(newDirection);
        }

        return result;
    }

    public boolean isOpposite(RoadsDirection other) {
        return Math.abs(other.index - this.index) == 2;
    }

    public boolean isNextClockwise(RoadsDirection other) {
        return (this.index + 1) % 4 == other.index;
    }
}
