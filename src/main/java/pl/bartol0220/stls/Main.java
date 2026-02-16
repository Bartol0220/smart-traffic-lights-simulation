package pl.bartol0220.stls;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.Road;
import pl.bartol0220.stls.model.lightControllers.*;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.List;

public class Main {
    static void main() {
        Intersection intersection = new Intersection();

        System.out.println(intersection);

        try {
            Road road = intersection.getRoad(RoadsDirection.NORTH);
            road.addTrafficLane(List.of(RoadsDirection.SOUTH, RoadsDirection.WEST));

            road = intersection.getRoad(RoadsDirection.SOUTH);
            road.addTrafficLane(List.of(RoadsDirection.NORTH, RoadsDirection.EAST));

            road = intersection.getRoad(RoadsDirection.EAST);
            road.addTrafficLane(List.of(RoadsDirection.WEST));

            road = intersection.getRoad(RoadsDirection.WEST);
            road.addTrafficLane(List.of(RoadsDirection.EAST));
        } catch (InvalidTrafficLaneDirectionException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println(intersection);

        LightPhaseGenerator lightPhaseGenerator = new LightPhaseGenerator();
        LightPhaseSequence lightPhases = lightPhaseGenerator.generateLightPhases(intersection);

        System.out.println(lightPhases);

        try {
            AbstractLightController lightController = new TimeLightController(intersection);

            lightController.initLightController();

            System.out.println(intersection);

            for (int i = 0; i < 10; i++) {
                System.out.println("-----------\n" + (i + 1) + "\n-----------\n");
                lightController.step();
                System.out.println(intersection);
            }
        } catch (EmptyLightPhases e) {
            System.err.println(e.getMessage());
        }
    }
}
