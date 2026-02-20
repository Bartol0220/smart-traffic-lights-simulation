package pl.bartol0220.stls.model.vehicles;

import pl.bartol0220.stls.model.util.VehicleType;
import pl.bartol0220.stls.model.util.RoadsDirection;

public class Bus extends Vehicle {
    public Bus(String id, RoadsDirection startRoad, RoadsDirection endRoad) {
        super(id, startRoad, endRoad, 3);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.BUS;
    }
}
