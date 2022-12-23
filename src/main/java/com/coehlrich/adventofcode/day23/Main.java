package com.coehlrich.adventofcode.day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        String[] lines = input.lines().toArray(String[]::new);
        Map<Point, Elf> points = new HashMap<>();
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    points.put(new Point(x, y), new Elf(null));
                }
            }
        }

        List<Function<Point, Point>> proposals = new ArrayList<>();
        proposals.add((point) -> {
            for (int x = -1; x <= 1; x++) {
                if (points.containsKey(new Point(point.x() + x, point.y() - 1))) {
                    return null;
                }
            }
            return new Point(point.x(), point.y() - 1);
        });

        proposals.add((point) -> {
            for (int x = -1; x <= 1; x++) {
                if (points.containsKey(new Point(point.x() + x, point.y() + 1))) {
                    return null;
                }
            }
            return new Point(point.x(), point.y() + 1);
        });

        proposals.add((point) -> {
            for (int y = -1; y <= 1; y++) {
                if (points.containsKey(new Point(point.x() - 1, point.y() + y))) {
                    return null;
                }
            }
            return new Point(point.x() - 1, point.y());
        });

        proposals.add((point) -> {
            for (int y = -1; y <= 1; y++) {
                if (points.containsKey(new Point(point.x() + 1, point.y() + y))) {
                    return null;
                }
            }
            return new Point(point.x() + 1, point.y());
        });

        for (int i = 0; i < 10; i++) {
            doRound(points, proposals);
        }

        int minX = points.keySet().stream().mapToInt(Point::x).min().getAsInt();
        int maxX = points.keySet().stream().mapToInt(Point::x).max().getAsInt();
        int minY = points.keySet().stream().mapToInt(Point::y).min().getAsInt();
        int maxY = points.keySet().stream().mapToInt(Point::y).max().getAsInt();

        int count = 0;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
//                System.out.print(points.containsKey(new Point(x, y)) ? '#' : '.');
                if (!points.containsKey(new Point(x, y))) {
                    count++;
                }
            }
//            System.out.println();
        }

        int roundCount = 10;
        while (doRound(points, proposals)) {
            roundCount++;
        }

        return new Result(count, roundCount + 1);
    }

    public boolean doRound(Map<Point, Elf> points, List<Function<Point, Point>> proposals) {
        boolean nomoves = true;
        for (Map.Entry<Point, Elf> elves : points.entrySet()) {
            boolean nomove = true;
            loop: for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x != 0 || y != 0) {
                        nomove = !points.containsKey(new Point(elves.getKey().x() + x, elves.getKey().y() + y));
                        if (!nomove) {
                            break loop;
                        }
                    }
                }
            }
            if (!nomove) {
                nomoves = false;
                Point point = elves.getKey();
                for (Function<Point, Point> proposal : proposals) {
                    Point newPoint = proposal.apply(point);
                    if (newPoint != null) {
                        point = newPoint;
                        break;
                    }
                }
                elves.setValue(new Elf(point));
            } else {
                elves.setValue(new Elf(elves.getKey()));
            }
        }

        Map<Point, Point> canMove = new HashMap<>();
        Set<Point> cannotMove = new HashSet<>();

        for (Map.Entry<Point, Elf> elf : points.entrySet()) {
            if (!cannotMove.contains(elf.getValue().proposed())) {
                if (canMove.put(elf.getValue().proposed(), elf.getKey()) != null) {
                    canMove.remove(elf.getValue().proposed());
                    cannotMove.add(elf.getValue().proposed());
                }
            }
        }

        for (Map.Entry<Point, Point> move : canMove.entrySet()) {
            points.put(move.getKey(), points.remove(move.getValue()));
        }

//        int minX = points.keySet().stream().mapToInt(Point::x).min().getAsInt();
//        int maxX = points.keySet().stream().mapToInt(Point::x).max().getAsInt();
//        int minY = points.keySet().stream().mapToInt(Point::y).min().getAsInt();
//        int maxY = points.keySet().stream().mapToInt(Point::y).max().getAsInt();
//
//        System.out.println("== End of Round " + (i + 1) + " ==");
//        for (int y = 0; y < 12; y++) {
//            for (int x = 0; x < 14; x++) {
//                System.out.print(points.containsKey(new Point(x, y)) ? '#' : '.');
//            }
//            System.out.println();
//        }
//        System.out.println();
        proposals.add(proposals.remove(0));
        return !nomoves;
    }

}
