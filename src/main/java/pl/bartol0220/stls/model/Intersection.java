package pl.bartol0220.stls.model;

import java.util.EnumMap;
import java.util.Map;

public class Intersection {
    private final Map<RoadsDirection, Road> roads = new EnumMap<>(RoadsDirection.class);

    public Intersection() {
        for (RoadsDirection direction : RoadsDirection.values()){
            roads.put(direction, new Road(direction, 1));
        }
    }

    public Road getRoad(RoadsDirection direction) {
        return roads.get(direction);
    }
}
