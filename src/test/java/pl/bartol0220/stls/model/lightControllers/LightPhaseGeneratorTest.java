package pl.bartol0220.stls.model.lightControllers;

import org.junit.jupiter.api.Test;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.TrafficLane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LightPhaseGeneratorTest {

    @Test
    void checkPathCrossingWorks() {
        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        //First is U-turn
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.SOUTH, RoadsDirection.SOUTH));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.WEST, RoadsDirection.SOUTH));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.SOUTH, RoadsDirection.NORTH));
        //Symetric
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.NORTH));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.EAST, RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.NORTH));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.WEST, RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.NORTH));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.NORTH));

        //First is Straight
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.NORTH));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.EAST));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.WEST));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.SOUTH));
        //Symetric
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.SOUTH));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.EAST, RoadsDirection.NORTH, RoadsDirection.SOUTH));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.WEST, RoadsDirection.NORTH, RoadsDirection.SOUTH));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.SOUTH));

        //First is Left turn
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH, RoadsDirection.WEST));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH, RoadsDirection.SOUTH));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH, RoadsDirection.EAST));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH, RoadsDirection.NORTH));
        //Symetric
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.WEST, RoadsDirection.NORTH, RoadsDirection.EAST));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.EAST));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.EAST, RoadsDirection.NORTH, RoadsDirection.EAST));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.EAST));

        //First is Right turn
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.WEST, RoadsDirection.SOUTH, RoadsDirection.NORTH));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.WEST, RoadsDirection.SOUTH, RoadsDirection.EAST));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.WEST, RoadsDirection.SOUTH, RoadsDirection.SOUTH));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.NORTH, RoadsDirection.WEST, RoadsDirection.SOUTH, RoadsDirection.WEST));
        //Symetric
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.NORTH, RoadsDirection.WEST));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.EAST, RoadsDirection.NORTH, RoadsDirection.WEST));
        assertFalse(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.SOUTH, RoadsDirection.NORTH, RoadsDirection.WEST));
        assertTrue(lightPhaseGenerator.checkPathCrossing(RoadsDirection.SOUTH, RoadsDirection.WEST, RoadsDirection.NORTH, RoadsDirection.WEST));
    }

    private void testLanesCoverageAndCollisions(Intersection intersection, LightPhaseSequence lightPhases, LightPhaseGenerator lightPhaseGenerator) {
        Set<TrafficLane> allLanes = new HashSet<>(intersection.getAllLanes());

        Set<TrafficLane> lanesInPhases = lightPhases.lightPhases().stream()
                .flatMap(phase -> phase.getTrafficLanes().stream())
                .collect(Collectors.toSet());

        assertEquals(allLanes, lanesInPhases);

        for (LightPhase phase : lightPhases.lightPhases()) {
            List<TrafficLane> lanes = new ArrayList<>(phase.getTrafficLanes());
            for (int i = 0; i < lanes.size(); i++) {
                for (int j = i + 1; j < lanes.size(); j++) {
                    TrafficLane lane1 = lanes.get(i);
                    TrafficLane lane2 = lanes.get(j);
                    assertFalse(lightPhaseGenerator.checkCollision(lane1, lane2));
                }
            }
        }
    }

    @Test
    void generateLightPhaseWorks0() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.SOUTH));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.NORTH));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.EAST));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.WEST));

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        testLanesCoverageAndCollisions(intersection, lightPhases, lightPhaseGenerator);
    }

    @Test
    void generateLightPhaseWorks1() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.SOUTH));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.NORTH));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.EAST));

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        testLanesCoverageAndCollisions(intersection, lightPhases, lightPhaseGenerator);
    }

    @Test
    void generateLightPhaseWorks2() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.EAST));

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        testLanesCoverageAndCollisions(intersection, lightPhases, lightPhaseGenerator);
    }

    @Test
    void generateLightPhaseWorks3() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.EAST, RoadsDirection.SOUTH, RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.WEST, RoadsDirection.NORTH, RoadsDirection.EAST));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST, RoadsDirection.NORTH));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST, RoadsDirection.SOUTH));

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        testLanesCoverageAndCollisions(intersection, lightPhases, lightPhaseGenerator);
    }

    @Test
    void generateLightPhaseWorks4() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.WEST));
        road.addTrafficLane(List.of(RoadsDirection.SOUTH));
        road.addTrafficLane(List.of(RoadsDirection.SOUTH));
        road.addTrafficLane(List.of(RoadsDirection.EAST));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.EAST));
        road.addTrafficLane(List.of(RoadsDirection.NORTH));
        road.addTrafficLane(List.of(RoadsDirection.NORTH));
        road.addTrafficLane(List.of(RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.EAST));

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        testLanesCoverageAndCollisions(intersection, lightPhases, lightPhaseGenerator);
    }

    @Test
    void generateLightPhaseWorks5() {
        Intersection intersection = new Intersection();

        Road road = intersection.getRoad(RoadsDirection.NORTH);
        road.addTrafficLane(List.of(RoadsDirection.WEST));
        road.addTrafficLane(List.of(RoadsDirection.SOUTH));
        road.addTrafficLane(List.of(RoadsDirection.SOUTH));
        road.addTrafficLane(List.of(RoadsDirection.EAST));

        road = intersection.getRoad(RoadsDirection.SOUTH);
        road.addTrafficLane(List.of(RoadsDirection.EAST));
        road.addTrafficLane(List.of(RoadsDirection.NORTH));
        road.addTrafficLane(List.of(RoadsDirection.NORTH));
        road.addTrafficLane(List.of(RoadsDirection.WEST));

        road = intersection.getRoad(RoadsDirection.EAST);
        road.addTrafficLane(List.of(RoadsDirection.WEST, RoadsDirection.NORTH, RoadsDirection.SOUTH));

        road = intersection.getRoad(RoadsDirection.WEST);
        road.addTrafficLane(List.of(RoadsDirection.EAST, RoadsDirection.NORTH, RoadsDirection.SOUTH));

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        testLanesCoverageAndCollisions(intersection, lightPhases, lightPhaseGenerator);
    }
}