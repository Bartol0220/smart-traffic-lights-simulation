package pl.bartol0220.stls.exceptions;

import pl.bartol0220.stls.model.util.RoadsDirection;

public class IllegalVehicleDestination extends Exception {
    public IllegalVehicleDestination(RoadsDirection destination) {
        super("Illegal vehicle destination: " + destination);
    }
}
