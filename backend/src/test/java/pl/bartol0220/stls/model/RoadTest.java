package pl.bartol0220.stls.model;

import org.junit.jupiter.api.Test;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoadTest {
    @Test
    void addTrafficLaneWorks() {
        Road road = new Road(RoadsDirection.SOUTH, 1);

        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.NORTH)));
        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.SOUTH)));
        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.WEST)));
        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.NORTH)));
        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST)));
    }

    @Test
    void addTrafficLaneThrowsOnInvalidDirections() {
        Road road = new Road(RoadsDirection.SOUTH, 1);

        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.NORTH)));

        assertThrows(InvalidTrafficLaneDirectionException.class, () -> {
            road.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.WEST));
        });

        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST)));
        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.SOUTH)));

        assertThrows(InvalidTrafficLaneDirectionException.class, () -> {
            road.addTrafficLane(List.of(RoadsDirection.WEST, RoadsDirection.EAST));
        });

        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.EAST)));
        assertDoesNotThrow(() -> road.addTrafficLane(List.of(RoadsDirection.EAST, RoadsDirection.NORTH)));

        assertThrows(InvalidTrafficLaneDirectionException.class, () -> {
            road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.NORTH));
        });

        assertThrows(InvalidTrafficLaneDirectionException.class, () -> {
            road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.EAST));
        });
    }
}