package com.coehlrich.adventofcode.day15;

import java.util.ArrayList;
import java.util.List;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.ints.IntComparators;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        int part1Line = 2_000_000;
        Sensor[] sensors = input.lines().map(Sensor::parse).toArray(Sensor[]::new);

//        LongSet longset = new LongOpenHashBigSet();
//        Object2CharMap<Point> map = new Object2CharOpenHashMap<>();
//        for (long i = 0l; i < 20 * 20l; i++) {
//            longset.add(i);
//        }
//        for (Sensor sensor : sensors) {
//            map.put(sensor.pos(), 'S');
//            map.put(sensor.beacon(), 'B');
//            for (int i = sensor.pos().manhatten(sensor.beacon()); i <= sensor.pos().manhatten(sensor.beacon()); i++) {
//                for (int y = 0; y <= i; y++) {
//                    char c = i == sensor.pos().manhatten(sensor.beacon()) ? 'E' : '#';
//                    map.putIfAbsent(new Point(sensor.pos().x() + (i - y), sensor.pos().y() + y), c);
//                    map.putIfAbsent(new Point(sensor.pos().x() + (i - y), sensor.pos().y() - y), c);
//                    map.putIfAbsent(new Point(sensor.pos().x() - (i - y), sensor.pos().y() + y), c);
//                    map.putIfAbsent(new Point(sensor.pos().x() - (i - y), sensor.pos().y() - y), c);
//                }
//            }
//        }

//        long max = 20;
//
//        System.out.println(
//                "Average Sensor x: " + Stream.of(sensors).map(Sensor::pos).mapToInt(Point::x).summaryStatistics());
//        System.out.println(
//                "Average Sensor y: " + Stream.of(sensors).map(Sensor::pos).mapToInt(Point::y).summaryStatistics());
//        System.out.println(
//                "Average Beacon x: "
//                        + Stream.of(sensors).map(Sensor::beacon).distinct().mapToInt(Point::x)
//                                .filter((bx) -> bx >= 0 && bx <= max).summaryStatistics());
//        System.out.println(
//                "Average Beacon y: "
//                        + Stream.of(sensors).map(Sensor::beacon).distinct().mapToInt(Point::y)
//                                .filter((by) -> by >= 0 && by <= max).summaryStatistics());
//
//
//        IntSummaryStatistics x = Stream.of(sensors).map(Sensor::beacon).distinct().mapToInt(Point::x)
//                .filter((bx) -> bx >= 0 && bx <= max)
//                .summaryStatistics();
//        IntSummaryStatistics y = Stream.of(sensors).map(Sensor::beacon).distinct().mapToInt(Point::y)
//                .filter((by) -> by >= 0 && by <= max)
//                .summaryStatistics();
//        long bx = Math.round(x.getAverage()) + 2;
//        long by = Math.round(y.getAverage()) + 2;
//
//        System.out.println(bx);
//        System.out.println(by);
//        System.out.println(bx * 4_000_000 + by);
//        map.put(new Point(14, 11), 'A');
//        for (int y = -10; y < 30; y++) {
//            for (int x = -10; x < 30; x++) {
//                System.out.print(map.getOrDefault(new Point(x, y), '.'));
//            }
//            System.out.println();
//        }
//        set.intStream().sorted().forEach(System.out::println);

        long y = 0;
        long x = 0;
        for (int i = 0; i < 4_000_000; i++) {
//            if (i % 100000 == 0) {
//                System.out.println(i);
//            }
//        for (int i = 0; i < 20; i++) {
            List<Range> used = getUsed(sensors, i);
            if (used.size() >= 2) {
                x = used.get(0).end() + 1;
                y = i;
            }
        }
//        return new Result(getUsed(sensors, 10).stream().mapToInt(Range::getLength).sum(), x * 4_000_000 + y);
        return new Result(getUsed(sensors, 2_000_000).stream().mapToInt(Range::getLength).sum(), x * 4_000_000 + y);
    }

    private List<Range> getUsed(Sensor[] sensors, int line) {
        List<Range> ranges = new ArrayList<>();
        for (Sensor sensor : sensors) {
            int manhatten = sensor.pos().manhatten(sensor.beacon());
            // int dist = Math.abs(sensor.pos().y() - 2_000_000);
            int dist = Math.abs(sensor.pos().y() - line);
            if (manhatten >= dist) {
                ranges.add(new Range(sensor.pos().x() - (manhatten - dist), sensor.pos().x() + (manhatten - dist)));
            }
        }

        ranges.sort((r1, r2) -> IntComparators.NATURAL_COMPARATOR.compare(r1.start(), r2.start()));
        List<Range> merged = new ArrayList<>();

        int min = Integer.MIN_VALUE;
        int max = Integer.MIN_VALUE;
        for (Range range : ranges) {
            if (min == Integer.MIN_VALUE && max == Integer.MIN_VALUE) {
                min = range.start();
                max = range.end();
            } else {
                if (max + 1 >= range.start() && max < range.end()) {
                    max = range.end();
                } else if (max < range.start()) {
                    merged.add(new Range(min, max));
                    min = range.start();
                    max = range.end();
                }
            }
        }
        if (min != Integer.MIN_VALUE && max != Integer.MIN_VALUE) {
            merged.add(new Range(min, max));
        }
        return merged;
    }

}
