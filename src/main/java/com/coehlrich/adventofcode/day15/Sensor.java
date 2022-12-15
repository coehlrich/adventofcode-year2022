package com.coehlrich.adventofcode.day15;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Sensor(Point pos, Point beacon) {

    private static Pattern SENSOR = Pattern
            .compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

    public static Sensor parse(String line) {
        Matcher matcher = SENSOR.matcher(line);
        matcher.matches();
        return new Sensor(new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))));
    }

}
