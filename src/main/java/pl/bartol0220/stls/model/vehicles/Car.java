package pl.bartol0220.stls.model.vehicles;

import pl.bartol0220.stls.model.RoadsDirection;

public class Car extends Vehicle {
    public Car(String id, RoadsDirection startRoad, RoadsDirection endRoad) {
        super(id, startRoad, endRoad, 1);
    }
}
