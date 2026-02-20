package pl.bartol0220.stls.model.vehicles;

import pl.bartol0220.stls.model.util.VehicleType;
import pl.bartol0220.stls.model.util.RoadsDirection;

public class VehicleFactory {
    public Vehicle create(VehicleType type, String vehicleId, RoadsDirection startRoad, RoadsDirection endRoad) {
        return switch (type) {
            case CAR -> new Car(vehicleId, startRoad, endRoad);
            case AMBULANCE -> new Ambulance(vehicleId, startRoad, endRoad);
            case BUS -> new Bus(vehicleId, startRoad, endRoad);
        };
    }
}
