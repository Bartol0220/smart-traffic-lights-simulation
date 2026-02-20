package pl.bartol0220.stls.model.vehicles;

import pl.bartol0220.stls.model.util.VehicleType;
import pl.bartol0220.stls.model.util.RoadsDirection;

public class Ambulance extends Vehicle{
    public Ambulance(String id, RoadsDirection startRoad, RoadsDirection endRoad) {
        super(id, startRoad, endRoad, 1);
    }

    @Override
    public boolean isEmergencyVehicle() {
        return true;
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.AMBULANCE;
    }
}
