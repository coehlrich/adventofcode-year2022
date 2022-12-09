package com.coehlrich.adventofcode.day9;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Movement> movements = input.lines().map(Movement::parse).toList();

        return new Result(simulate(movements, 2), simulate(movements, 10));
    }

    public int simulate(List<Movement> movements, int count) {
        List<Point> knots = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            knots.add(new Point(0, 0));
        }
        Set<Point> points = new HashSet<>();
        points.add(knots.get(0));

        for (Movement movement : movements) {
            for (int i = 0; i < movement.distance(); i++) {
                Point h = knots.get(0);
                h = new Point(h.x() + switch (movement.direction()) {
                    case 'L' -> -1;
                    case 'R' -> 1;
                    default -> 0;
                }, h.y() + switch (movement.direction()) {
                    case 'U' -> -1;
                    case 'D' -> 1;
                    default -> 0;
                });
                knots.set(0, h);
                for (int j = 1; j < knots.size(); j++) {
                    h = knots.get(j - 1);
                    Point t = knots.get(j);
                    int diffX = Math.abs(h.x() - t.x());
                    int diffY = Math.abs(h.y() - t.y());

                    if (diffX > 1 || diffY > 1) {
                        int x = h.x() < t.x() ? t.x() - 1 : h.x() == t.x() ? t.x() : t.x() + 1;
                        int y = h.y() < t.y() ? t.y() - 1 : h.y() == t.y() ? t.y() : t.y() + 1;
                        t = new Point(x, y);
                    }
                    knots.set(j, t);
                    //
                    // if (diffX > 1) {
                    // t = new Point(h.x() < t.x() ? t.x() - 1 : t.x() + 1, t.y());
                    // }
                    //
                    // if (diffY > 1) {
                    // t = new Point(t.x(), h.y() < t.y() ? t.y() - 1 : t.y() + 1);
                    // }
                }
                points.add(knots.get(knots.size() - 1));
            }
        }
        return points.size();
    }

}
