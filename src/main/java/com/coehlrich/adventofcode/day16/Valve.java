package com.coehlrich.adventofcode.day16;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public record Valve(String room, int flowRate, List<Tunnel> tunnels) {

    private static Pattern VALVE = Pattern
            .compile("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnels? leads? to valves? (.*)");

    public static Valve parse(String line) {
        Matcher matcher = VALVE.matcher(line);
        matcher.matches();
        String room = matcher.group(1);
        int flowRate = Integer.parseInt(matcher.group(2));
        List<Tunnel> tunnels = Stream.of(matcher.group(3).split(", ")).map(Tunnel::parse).toList();
        return new Valve(room, flowRate, tunnels);
    }

}
