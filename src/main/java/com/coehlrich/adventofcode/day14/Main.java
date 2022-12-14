package com.coehlrich.adventofcode.day14;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<Point, Tile> map = new HashMap<>();
        int maxY = 0;
        for (String line : input.lines().toList()) {
            Point point = null;
            for (String pointS : line.split(" -> ")) {
                int[] split = Stream.of(pointS.split(",")).mapToInt(Integer::parseInt).toArray();
                Point newP = new Point(split[0], split[1]);
                maxY = Math.max(maxY, newP.y());
                if (point != null) {
                    point.getPointsTo(newP).forEach((stone) -> map.put(stone, Tile.STONE));
                }
                point = newP;
            }
        }
        Map<Point, Tile> clone = new HashMap<>(map);
        int part1 = 0;
        while (placeSand(new Point(500, 0), map, maxY, false)) {
            part1++;
        }

        int part2 = 0;
        while (placeSand(new Point(500, 0), clone, maxY, true)) {
            part2++;
        }
        return new Result(part1, part2 + 1);
    }

    private boolean placeSand(Point point, Map<Point, Tile> map, int maxY, boolean part2) {
        int x = point.x();
        int y = point.y();
        if (y > maxY && !part2) {
            return false;
        } else if (y > maxY) {
            map.put(new Point(x, y), Tile.SAND);
            return true;
        } else if (!map.containsKey(new Point(x, y + 1))) {
            return placeSand(new Point(x, y + 1), map, maxY, part2);
        } else if (!map.containsKey(new Point(x - 1, y + 1))) {
            return placeSand(new Point(x - 1, y + 1), map, maxY, part2);
        } else if (!map.containsKey(new Point(x + 1, y + 1))) {
            return placeSand(new Point(x + 1, y + 1), map, maxY, part2);
        } else if (x == 500 && y == 0 && part2) {
            return false;
        } else {
            map.put(point, Tile.SAND);
            return true;
        }
    }

}
