package pl.bartol0220.stls.model.lightControllers;

import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.util.LaneType;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.TrafficLane;

import java.util.*;

public class LightPhaseGenerator {
    public List<LightPhase> GenerateLightPhases(Intersection intersection) {
        Set<LightPhase> lightPhases = new HashSet<>();
        List<TrafficLane> allLanes = intersection.getAllLanes();
        Map<TrafficLane, Set<TrafficLane>> collisionMap = getCollisionMap(allLanes);

        Set<TrafficLane> uncovered = new HashSet<>(allLanes);

        while (!uncovered.isEmpty()) {
            TrafficLane trafficLane = uncovered
                    .stream()
                    .max(Comparator.comparingInt(TrafficLane::getLanePriority))
                    .orElseThrow();

            LightPhase phase = new LightPhase();
            phase.addTrafficLane(trafficLane);

            Set<TrafficLane> blocked = new HashSet<>(collisionMap.get(trafficLane));

            List<TrafficLane> sortedUncovered = uncovered.stream()
                    .sorted(Comparator.comparingInt(TrafficLane::getLanePriority).reversed())
                    .toList();

            // add all possible uncovered lanes
            for (TrafficLane candidate : sortedUncovered) {
                if (candidate == trafficLane) continue;

                if (!blocked.contains(candidate)) {
                    phase.addTrafficLane(candidate);
                    blocked.addAll(collisionMap.get(candidate));
                }
            }

            uncovered.removeAll(phase.getTrafficLanes());

            List<TrafficLane> sortedAll = allLanes.stream()
                    .sorted(Comparator.comparingInt(TrafficLane::getLanePriority).reversed())
                    .toList();

            // add all possible already covered lanes (maximize size)
            for (TrafficLane candidate : sortedAll) {
                if (phase.getTrafficLanes().contains(candidate)) continue;

                if (!blocked.contains(candidate)) {
                    phase.addTrafficLane(candidate);
                    blocked.addAll(collisionMap.get(candidate));
                }
            }

            lightPhases.add(phase);
        }

        return lightPhases.stream().toList();
    }

    private Map<TrafficLane, Set<TrafficLane>> getCollisionMap(List<TrafficLane> allLanes) {
        Map<TrafficLane, Set<TrafficLane>> collisionMap = new HashMap<>();
        int numLanes = allLanes.size();

        for(TrafficLane trafficLane : allLanes) {
            collisionMap.put(trafficLane, new HashSet<>());
        }

        for(int i = 0; i < numLanes; i++) {
            TrafficLane trafficLane1 = allLanes.get(i);
            for (int j = i + 1; j < numLanes; j++) {
                TrafficLane trafficLane2 = allLanes.get(j);
                if (checkCollision(trafficLane1, trafficLane2)) {
                    collisionMap.get(trafficLane1).add(trafficLane2);
                    collisionMap.get(trafficLane2).add(trafficLane1);
                }
            }
        }
        return collisionMap;
    }

    boolean checkCollision(TrafficLane tl1, TrafficLane tl2) {
        RoadsDirection tl1EntryDirection = tl1.getEntryDirection();
        RoadsDirection tl2EntryDirection = tl2.getEntryDirection();

        if (tl1EntryDirection == tl2EntryDirection) {
            return false;
        }

        Set<RoadsDirection> tl1ExitDirections = tl1.getExitDirections();
        Set<RoadsDirection> tl2ExitDirections = tl2.getExitDirections();

        if (!Collections.disjoint(tl1ExitDirections, tl2ExitDirections)) {
            return true;
        }

        for (RoadsDirection tl1ExitDirection : tl1ExitDirections) {
            for (RoadsDirection tl2ExitDirection : tl2ExitDirections) {
                if (checkPathCrossing(tl1EntryDirection, tl1ExitDirection, tl2EntryDirection, tl2ExitDirection)) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean checkPathCrossing(
            RoadsDirection entry1,
            RoadsDirection exit1,
            RoadsDirection entry2,
            RoadsDirection exit2
    ) {
        LaneType type1 = exit1.getLaneType(entry1);
        LaneType type2 = exit2.getLaneType(entry2);

        if (entry1.isOpposite(entry2)) {
            if (type1 == LaneType.STRAIGHT && (type2 == LaneType.LEFT || type2 == LaneType.U_TURN)) return true;
            if (type2 == LaneType.STRAIGHT && (type1 == LaneType.LEFT || type1 == LaneType.U_TURN)) return true;

            if (type1 == LaneType.LEFT && type2 == LaneType.RIGHT) return true;
            if (type2 == LaneType.LEFT && type1 == LaneType.RIGHT) return true;

            return  false;
        }

        if (entry1.isNextClockwise(entry2)) {
            if (type1 == LaneType.STRAIGHT && (type2 == LaneType.RIGHT || type2 == LaneType.U_TURN)) return false;
            if (type1 == LaneType.LEFT && type2 == LaneType.RIGHT) return false;
            if (type1 == LaneType.RIGHT && type2 != LaneType.STRAIGHT) return false;
            if (type1 == LaneType.U_TURN && (type2 == LaneType.LEFT || type2 == LaneType.U_TURN)) return false;

            return  true;
        }

        if (type1 == LaneType.STRAIGHT) return true;
        if (type1 == LaneType.LEFT && (type2 == LaneType.RIGHT || type2 == LaneType.U_TURN)) return false;
        if (type1 == LaneType.RIGHT && type2 != LaneType.U_TURN) return false;
        if (type1 == LaneType.U_TURN && type2 != LaneType.LEFT) return false;

        return  true;
    }
}
