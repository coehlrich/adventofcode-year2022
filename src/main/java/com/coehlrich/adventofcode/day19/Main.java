package com.coehlrich.adventofcode.day19;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Blueprint[] blueprints = input.lines().map(Blueprint::parse).toArray(Blueprint[]::new);

        int[] part2 = Stream.of(blueprints).limit(3).mapToInt((blueprint) -> calculate(blueprint, 32)).toArray();

        return new Result(
                Stream.of(blueprints).mapToInt((blueprint) -> blueprint.id() * calculate(blueprint, 24)).sum(),
                part2[0] * part2[1] * part2[2]);
    }

    private int calculate(Blueprint blueprint, int minutes) {
        int max = 0;
//        if (minutes == 32) {
//            for (int oreR = 1; oreR <= 4; oreR++) {
//                for (int clayR = 1; clayR <= 10; clayR++) {
//                    for (int obsidianR = 1; obsidianR <= 10; obsidianR++) {
//
//                        Object2IntMap<Resource> resources = new Object2IntOpenHashMap<>();
//                        Object2IntMap<Resource> robots = new Object2IntOpenHashMap<>();
//                        robots.put(Resource.ORE, 1);
////                    boolean log = oreR == 3 && clayR == 5 && obsidianR == 6;
//                        boolean log = false;
//
//                        for (int i = 1; i <= minutes; i++) {
//                            Set<Resource> allowed = robots.getInt(Resource.ORE) < oreR ? Set.of(Resource.ORE)
//                                    : Set.of(Resource.values());
//
////                        Object2IntMap<Resource> turnsNeeded = new Object2IntOpenHashMap<>(Map.of(
////                                Resource.ORE,
////                                (int) Math.max(0,
////                                Math.ceil(
////                                        (float) (blueprint.robots().get(Resource.ORE).costs().getInt(Resource.ORE)
////                                                - resources.getInt(Resource.ORE)) / robots.getInt(Resource.ORE))),
////                                Resource.CLAY,
////                                (int) Math.max(0,
////                                Math.ceil(
////                                        (float) (blueprint.robots().get(Resource.CLAY).costs().getInt(Resource.ORE)
////                                                - resources.getInt(Resource.ORE)) / robots.getInt(Resource.ORE))),
////                                Resource.OBSIDIAN,
////                                (int) Math.max(0, Math.max(
////                                Math.ceil(
////                                        (float) (blueprint.robots().get(Resource.OBSIDIAN).costs().getInt(Resource.ORE)
////                                                - resources.getInt(Resource.ORE)) / robots.getInt(Resource.ORE)),
////                                Math.ceil(
////                                        (float) (blueprint.robots().get(Resource.OBSIDIAN).costs().getInt(Resource.CLAY)
////                                                - resources.getInt(Resource.CLAY)) / robots.getInt(Resource.CLAY))));
////
////                        int geodeTurnsNeeded = (int) Math.max(0, Math.max(
////                                Math.ceil(
////                                        (float) (blueprint.robots().get(Resource.GEODE).costs().getInt(Resource.ORE)
////                                                - resources.getInt(Resource.ORE)) / robots.getInt(Resource.ORE)),
////                                Math.ceil(
////                                        (float) (blueprint.robots().get(Resource.GEODE).costs()
////                                                .getInt(Resource.OBSIDIAN)
////                                                - resources.getInt(Resource.OBSIDIAN))
////                                                / robots.getInt(Resource.OBSIDIAN))));
//                            if (log) {
//                                System.out.println("== Minute " + i + " ==");
//
////                            System.out.println(oreTurnsNeeded);
////                            System.out.println(clayTurnsNeeded);
////                            System.out.println(obsidianTurnsNeeded);
////                            System.out.println(geodeTurnsNeeded);
//                            }
//                            Resource builtRobot = null;
//                            Object2IntMap<Resource> oreUsed = new Object2IntOpenHashMap<>();
//                            int oreUsedInt = 0;
//                            for (Resource resource : Resource.values()) {
//                                oreUsed.put(resource, oreUsedInt);
//                                if (resource.ordinal() >= Resource.CLAY.ordinal()) {
//                                    oreUsedInt = blueprint.robots().get(resource).costs().getInt(Resource.ORE);
//                                }
//                            }
//                            Stack<Robot> stack = new Stack<>();
//                            for (Resource robot : Resource.values()) {
//                                stack.add(blueprint.robots().get(robot));
//                            }
//                            while (!stack.isEmpty()) {
//                                Robot robot = stack.pop();
//                                if (!allowed.contains(robot.resource())) {
//                                    continue;
//                                }
//                                int needed = switch (robot.resource()) {
//                                    case ORE -> oreR;
//                                    case CLAY -> clayR;
//                                    case OBSIDIAN -> obsidianR;
//                                    case GEODE -> Integer.MAX_VALUE;
//                                };
//
//                                if (robots.getInt(robot.resource()) < needed) {
//                                    int turnsNeeded = 0;
//                                    for (Object2IntMap.Entry<Resource> cost : robot.costs().object2IntEntrySet()) {
//                                        turnsNeeded = Math.max(turnsNeeded, Math.max(0,
//                                                (int) Math.ceil(
//                                                        (float) (cost.getIntValue()
//                                                                - resources.getInt(cost.getKey()))
//                                                                / robots.getInt(cost.getKey()))));
//                                    }
//
//                                    if (turnsNeeded == Integer.MAX_VALUE) {
//                                        turnsNeeded = 10000;
//                                    }
//
//                                    if (log) {
//                                        System.out.println(robot.resource() + ": " + turnsNeeded);
//                                    }
//
//                                    if (oreUsed.getInt(robot.resource())
//                                            + robot.costs().getInt(Resource.ORE) > resources.getInt(Resource.ORE)
//                                                    + (turnsNeeded * robots.getInt(Resource.ORE))
//                                            && turnsNeeded > 0) {
//                                        break;
//                                    }
//                                    Object2IntMap<Resource> required = robot.costs();
//                                    boolean allHas = required.object2IntEntrySet().stream()
//                                            .allMatch(
//                                                    (entry) -> resources.getInt(entry.getKey()) >= entry.getIntValue());
//                                    if (allHas) {
//                                        for (Object2IntMap.Entry<Resource> cost : required.object2IntEntrySet()) {
//                                            resources.put(cost.getKey(),
//                                                    resources.getInt(cost.getKey()) - cost.getIntValue());
//                                        }
//                                        builtRobot = robot.resource();
//                                        if (log) {
//                                            System.out.println(
//                                                    "Spend to start building a " + robot.resource() + " robot");
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//                            for (Object2IntMap.Entry<Resource> robot : robots.object2IntEntrySet()) {
//                                resources.put(robot.getKey(), resources.getInt(robot.getKey()) + robot.getIntValue());
//                                if (log) {
//                                    System.out.println(robot.getIntValue() + " " + robot.getKey() + " robot collects "
//                                            + robot.getIntValue() + " " + robot.getKey() + "; you now have "
//                                            + resources.getInt(robot.getKey()));
//                                }
//                            }
//                            if (builtRobot != null) {
//                                robots.put(builtRobot, robots.getInt(builtRobot) + 1);
//                                if (log) {
//                                    System.out.println("The new " + builtRobot + " robot is ready; you now have "
//                                            + robots.getInt(builtRobot) + " of them.");
//                                }
//                            }
//                            if (log) {
//                                System.out.println();
//                            }
//                        }
//                        max = Math.max(resources.getInt(Resource.GEODE), max);
//                    }
//                }
//            }
//        } else {

        Set<State> states = new HashSet<>();
        states.add(new State(new Object2IntOpenHashMap<>(Map.of(Resource.ORE, 1)), new Object2IntOpenHashMap<>(), 1));
        int maxOreR = blueprint.robots().values().stream().map(Robot::costs).map(Object2IntMap::object2IntEntrySet)
                .flatMap(ObjectSet::stream).filter((entry) -> entry.getKey() == Resource.ORE)
                .mapToInt(Object2IntMap.Entry::getIntValue).max().getAsInt();
        System.out.println("------------");
//        System.out.println(maxOreR);
        while (!states.isEmpty()) {
            System.out.println(states.size());
            System.out.println(states.stream().findFirst().get().minutes());
//            System.out.println(max);
//            states = List.of(
//                    new State(new Object2IntOpenHashMap<>(Map.of(
//                            Resource.ORE, 1)), new Object2IntOpenHashMap<>(
//                                    Map.of(
//                                            Resource.ORE, 2)),
//                            3));
            Set<State> newStates = new HashSet<>();
            int i = 0;
            for (State state : states) {
                if (++i % 100000 == 0) {
                    System.out.println(i);
                }
                Object2IntMap<Resource> resources = state.resources();
                int geodeR = state.robots().getInt(Resource.GEODE);
                int possible = resources.getInt(Resource.GEODE);
                for (int j = state.minutes(); j <= minutes; j++) {
                    possible += geodeR++;
                }
                if (max >= possible) {
//                    System.out.println("Removing");
                    continue;
                }
                Object2IntMap<Resource> newResources = new Object2IntOpenHashMap<>(resources);
                for (Object2IntMap.Entry<Resource> robot : state.robots().object2IntEntrySet()) {
                    newResources.put(robot.getKey(), newResources.getInt(robot.getKey()) + robot.getIntValue());
                }
                int turnsLeft = minutes - state.minutes();
                newResources.put(Resource.ORE, Math.min(maxOreR * turnsLeft, newResources.getInt(Resource.ORE)));
                newResources.put(Resource.CLAY,
                        Math.min(blueprint.robots().get(Resource.OBSIDIAN).costs().getInt(Resource.CLAY) * turnsLeft,
                                newResources.getInt(Resource.CLAY)));
                newResources.put(Resource.OBSIDIAN,
                        Math.min(blueprint.robots().get(Resource.GEODE).costs().getInt(Resource.OBSIDIAN) * turnsLeft,
                                newResources.getInt(Resource.OBSIDIAN)));
//                System.out.println("resources: " + newResources);
                Object2IntMap<Resource> robots = state.robots();
                max = Math.max(
                        newResources.getInt(Resource.GEODE)
                                + (minutes - state.minutes()) * robots.getInt(Resource.GEODE),
                        max);
                int used = state.minutes();
                if (used < minutes) {
//                    System.out.println("Adding skip state");
                    List<Robot> toBuild = new ArrayList<>();
                    toBuild.addAll(blueprint.robots().values());
                    toBuild.remove(blueprint.robots().get(Resource.GEODE));
                    toBuild.add(0, blueprint.robots().get(Resource.GEODE));
                    boolean buildGeode = false;
                    for (Robot robot : toBuild) {
                        if (robot.resource() == Resource.ORE && maxOreR <= robots.getInt(Resource.ORE)) {
                            continue;
                        }
                        if (robot.resource().minutesRequired <= (minutes + 1) - used) {
                            Object2IntMap<Resource> required = robot.costs();
                            boolean allHas = required.object2IntEntrySet().stream()
                                    .allMatch((entry) -> resources.getInt(entry.getKey()) >= entry.getIntValue());
                            if (allHas) {
                                Object2IntMap<Resource> newRobots = new Object2IntOpenHashMap<>(robots);
                                Object2IntMap<Resource> newResourcesRobot = new Object2IntOpenHashMap<>(newResources);
                                for (Object2IntMap.Entry<Resource> cost : required.object2IntEntrySet()) {
                                    newResourcesRobot.put(cost.getKey(),
                                            newResourcesRobot.getInt(cost.getKey()) - cost.getIntValue());
                                }
                                newRobots.put(robot.resource(), newRobots.getInt(robot.resource()) + 1);
//                                System.out.println("Building robot: " + robot.resource());
//                                System.out.println("Resources with robot: " + newResourcesRobot);
//                                System.out.println("New robots: " + newRobots);
                                newStates.add(new State(newRobots, newResourcesRobot, used + 1));
                                if (robot.resource() == Resource.GEODE) {
                                    max = Math.max(
                                            newResourcesRobot.getInt(Resource.GEODE)
                                                    + ((minutes - 1) - state.minutes())
                                                            * newRobots.getInt(Resource.GEODE),
                                            max);
                                    buildGeode = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!buildGeode) {
                        newStates.add(new State(robots, newResources, used + 1));
                    }
                }
            }
            states = newStates;
//            return 0;
        }
//    }
        System.out.println(max);
        return max;
    }

}
