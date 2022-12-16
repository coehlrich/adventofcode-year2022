package com.coehlrich.adventofcode.day16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<String, Valve> valves = input.lines()
                .map(Valve::parse)
                .collect(Collectors.toMap(Valve::room, Function.identity()));

        for (Map.Entry<String, Valve> entry : valves.entrySet()) {
            Valve valve = entry.getValue();
            Object2IntMap<String> rooms = new Object2IntOpenHashMap<>();
            for (Tunnel tunnel : valve.tunnels()) {
                rooms.put(tunnel.end(), tunnel.cost());
            }

            boolean changed = true;
            while (changed) {
                changed = false;
                Object2IntMap<String> newRooms = new Object2IntOpenHashMap<>(rooms);
                for (String room : rooms.keySet()) {
                    Valve oValve = valves.get(room);
                    int cost = rooms.getInt(room);
                    for (Tunnel tunnel : oValve.tunnels()) {
                        int newCost = cost + tunnel.cost();
                        if (!newRooms.containsKey(tunnel.end()) || newCost < newRooms.getInt(tunnel.end())) {
                            newRooms.put(tunnel.end(), newCost);
                            changed = true;
                        }
                    }
                }
                rooms = newRooms;
            }
            rooms.removeInt(entry.getKey());
            List<Tunnel> tunnels = rooms.object2IntEntrySet().stream()
                    .filter((filter) -> valves.get(filter.getKey()).flowRate() > 0)
                    .map((map) -> new Tunnel(map.getIntValue(), map.getKey())).toList();
            entry.setValue(new Valve(valve.room(), valve.flowRate(), tunnels));
        }

        return new Result(execute(valves, false), execute(valves, true));
    }

    public int execute(Map<String, Valve> valves, boolean part2) {
        List<State> states = new ArrayList<>();
        states.add(new State("AA", part2 ? "AA" : "", Set.of(), 0, 0));
        int limit = part2 ? 26 : 30;
        int max = 0;
        List<State> current = new ArrayList<>(states);
        int roomsWithValves = (int) valves.values().stream().filter((valve) -> valve.flowRate() > 0).count() / 2;
        int roomsWithValvesOne = roomsWithValves + 1;
        while (!current.isEmpty()) {
            List<State> newStates = new ArrayList<>();
            for (State state : current) {
                Valve room = valves.get(state.room());
                for (Tunnel tunnel : room.tunnels()) {
                    if (!state.open().contains(tunnel.end()) && tunnel.cost() + state.minutes() < limit) {
                        Set<String> newOpen = new HashSet<>(state.open());
                        newOpen.add(tunnel.end());
                        int newCost = state.minutes() + tunnel.cost() + 1;
                        int score = state.score() + ((limit - newCost) * valves.get(tunnel.end()).flowRate());
                        max = Math.max(score, max);
                        newStates.add(new State(tunnel.end(), state.elephant(), newOpen, newCost, score));
                    }
                }

            }
            current = newStates;
            states.addAll(newStates);
        }
        if (part2) {
            max = 0;
            Map<Set<String>, State> lowest = new HashMap<>();
            for (State state : states) {
                if ((!lowest.containsKey(state.open()) || lowest.get(state.open()).score() < state.score())
                        && (state.open().size() == roomsWithValves || state.open().size() == roomsWithValvesOne)) {
                    lowest.put(state.open(), new State(state.room(), state.elephant(), state.open(), 0, state.score()));
                }
            }
            System.out.println(lowest.size());
//            System.out.println(lowest);
//            System.out.println(lowest.get(Set.of("JJ", "BB", "CC")));
            states = new ArrayList<>(lowest.values());
            while (!states.isEmpty()) {
                List<State> newStates = new ArrayList<>();
                for (State state : states) {
                    Valve room = valves.get(state.elephant());
                    for (Tunnel tunnel : room.tunnels()) {
                        if (!state.open().contains(tunnel.end()) && tunnel.cost() + state.minutes() < limit) {
                            Set<String> newOpen = new HashSet<>(state.open());
                            newOpen.add(tunnel.end());
                            int newCost = state.minutes() + tunnel.cost() + 1;
                            int score = state.score() + ((limit - newCost) * valves.get(tunnel.end()).flowRate());
                            max = Math.max(score, max);
                            newStates.add(new State(state.room(), tunnel.end(), newOpen, newCost, score));
                        }
                    }

                }
                states = newStates;
            }
        }
        return max;
    }

}
