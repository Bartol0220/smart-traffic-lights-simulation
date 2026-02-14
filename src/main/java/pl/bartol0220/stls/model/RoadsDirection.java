package pl.bartol0220.stls.model;

public enum RoadsDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public String toString(){
        return switch (this) {
            case NORTH -> "N";
            case EAST -> "E";
            case SOUTH -> "S";
            case WEST -> "W";
        };
    }
}
