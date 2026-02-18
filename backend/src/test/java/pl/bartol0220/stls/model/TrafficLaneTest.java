package pl.bartol0220.stls.model;

import org.junit.jupiter.api.Test;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLaneTest {
    @Test
    void compareWorksWithOneDirection() {
        TrafficLane lane1 = new TrafficLane(0, 0, RoadsDirection.SOUTH);
        TrafficLane lane2 = new TrafficLane(1, 0, RoadsDirection.SOUTH);

        lane1.addDirection(RoadsDirection.SOUTH);
        lane2.addDirection(RoadsDirection.WEST);
        List<TrafficLane> lanes = new ArrayList<>(Arrays.asList(lane1, lane2));
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane1, lanes.getFirst());

        lane1.removeDirection(RoadsDirection.SOUTH);
        lane1.addDirection(RoadsDirection.NORTH);
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane2, lanes.getFirst());

        lane1.removeDirection(RoadsDirection.NORTH);
        lane1.addDirection(RoadsDirection.EAST);
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane2, lanes.getFirst());

        lane2.removeDirection(RoadsDirection.WEST);
        lane2.addDirection(RoadsDirection.NORTH);
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane2, lanes.getFirst());

        lane2.removeDirection(RoadsDirection.NORTH);
        lane2.addDirection(RoadsDirection.EAST);
        lane1.removeDirection(RoadsDirection.EAST);
        lane1.addDirection(RoadsDirection.WEST);
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane1, lanes.getFirst());
    }

    @Test
    void compareWorksWithMultipleDirections() {
        TrafficLane lane1 = new TrafficLane(0, 0, RoadsDirection.SOUTH);
        TrafficLane lane2 = new TrafficLane(1, 0, RoadsDirection.SOUTH);

        lane1.addDirection(RoadsDirection.SOUTH);
        lane1.addDirection(RoadsDirection.WEST);
        lane2.addDirection(RoadsDirection.NORTH);
        List<TrafficLane> lanes = new ArrayList<>(Arrays.asList(lane1, lane2));
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane1, lanes.getFirst());

        lane1.removeDirection(RoadsDirection.WEST);
        lane1.addDirection(RoadsDirection.NORTH);
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane1, lanes.getFirst());

        lane1.removeDirection(RoadsDirection.SOUTH);
        lane1.addDirection(RoadsDirection.EAST);
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane2, lanes.getFirst());

        lane1 = new TrafficLane(0, 0, RoadsDirection.SOUTH);
        lane2 = new TrafficLane(1, 0, RoadsDirection.SOUTH);

        lane1.addDirection(RoadsDirection.SOUTH);
        lane1.addDirection(RoadsDirection.NORTH);
        lane2.addDirection(RoadsDirection.WEST);
        lanes = new ArrayList<>(Arrays.asList(lane1, lane2));
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertEquals(lane1, lanes.getFirst());
    }

    private void assertArrayEquals(ArrayList<TrafficLane> trafficLanes, ArrayList<TrafficLane> trafficLanes1) {
        assertEquals(trafficLanes.size(), trafficLanes1.size());
        for (int i = 0; i < trafficLanes.size(); i++) {
            assertEquals(trafficLanes.get(i), trafficLanes1.get(i));
        }
    }

    @Test
    void compareWorksWithMultipleLines0() {
        TrafficLane lane1 = new TrafficLane(0, 0, RoadsDirection.SOUTH);
        TrafficLane lane2 = new TrafficLane(1, 0, RoadsDirection.SOUTH);
        TrafficLane lane3 = new TrafficLane(2, 0, RoadsDirection.SOUTH);

        lane3.addDirection(RoadsDirection.SOUTH);
        lane3.addDirection(RoadsDirection.WEST);

        lane2.addDirection(RoadsDirection.NORTH);

        lane1.addDirection(RoadsDirection.NORTH);
        lane1.addDirection(RoadsDirection.EAST);

        ArrayList<TrafficLane> lanes = new ArrayList<>(Arrays.asList(lane1, lane2, lane3));
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertArrayEquals(new ArrayList<>(Arrays.asList(lane3, lane2, lane1)), lanes);
    }

    @Test
    void compareWorksWithMultipleLines1() {
        TrafficLane lane1 = new TrafficLane(0, 0, RoadsDirection.SOUTH);
        TrafficLane lane2 = new TrafficLane(1, 0, RoadsDirection.SOUTH);
        TrafficLane lane3 = new TrafficLane(2, 0, RoadsDirection.SOUTH);

        lane3.addDirection(RoadsDirection.SOUTH);
        lane3.addDirection(RoadsDirection.WEST);
        lane3.addDirection(RoadsDirection.EAST);

        lane2.addDirection(RoadsDirection.NORTH);

        lane1.addDirection(RoadsDirection.NORTH);
        lane1.addDirection(RoadsDirection.EAST);

        ArrayList<TrafficLane> lanes = new ArrayList<>(Arrays.asList(lane1, lane2, lane3));
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertArrayEquals(new ArrayList<>(Arrays.asList(lane3, lane2, lane1)), lanes);
    }

    @Test
    void compareWorksWithMultipleLines2() {
        TrafficLane lane1 = new TrafficLane(0, 0, RoadsDirection.SOUTH);
        TrafficLane lane2 = new TrafficLane(1, 0, RoadsDirection.SOUTH);
        TrafficLane lane3 = new TrafficLane(2, 0, RoadsDirection.SOUTH);
        TrafficLane lane4 = new TrafficLane(3, 0, RoadsDirection.SOUTH);

        lane1.addDirection(RoadsDirection.SOUTH);

        lane2.addDirection(RoadsDirection.WEST);

        lane3.addDirection(RoadsDirection.NORTH);

        lane4.addDirection(RoadsDirection.EAST);

        ArrayList<TrafficLane> lanes = new ArrayList<>(Arrays.asList(lane1, lane2, lane3, lane4));
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertArrayEquals(new ArrayList<>(Arrays.asList(lane1, lane2, lane3, lane4)), lanes);
    }

    @Test
    void compareWorksWithMultipleLines3() {
        TrafficLane lane1 = new TrafficLane(0, 0, RoadsDirection.SOUTH);
        TrafficLane lane2 = new TrafficLane(1, 0, RoadsDirection.SOUTH);
        TrafficLane lane3 = new TrafficLane(2, 0, RoadsDirection.SOUTH);
        TrafficLane lane4 = new TrafficLane(3, 0, RoadsDirection.SOUTH);

        lane1.addDirection(RoadsDirection.SOUTH);
        lane1.addDirection(RoadsDirection.WEST);

        lane2.addDirection(RoadsDirection.SOUTH);
        lane2.addDirection(RoadsDirection.WEST);
        lane2.addDirection(RoadsDirection.NORTH);

        lane3.addDirection(RoadsDirection.WEST);
        lane3.addDirection(RoadsDirection.NORTH);

        lane4.addDirection(RoadsDirection.EAST);

        ArrayList<TrafficLane> lanes = new ArrayList<>(Arrays.asList(lane1, lane2, lane3, lane4));
        lanes.sort(TrafficLane.VISUAL_ORDER);

        assertArrayEquals(new ArrayList<>(Arrays.asList(lane1, lane2, lane3, lane4)), lanes);
    }
}