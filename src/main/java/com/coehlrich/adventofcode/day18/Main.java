package com.coehlrich.adventofcode.day18;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Set<Position> positions = input.lines().map(Position::parse).collect(Collectors.toSet());

        Set<Position> exterior = new HashSet<>();

        Queue<Position> queue = new ArrayDeque<>();
        queue.add(new Position(-1, -1, -1));

        int sx = positions.stream().mapToInt(Position::x).max().getAsInt();
        int sy = positions.stream().mapToInt(Position::y).max().getAsInt();
        int sz = positions.stream().mapToInt(Position::z).max().getAsInt();

        while (!queue.isEmpty()) {
            Position pos = queue.poll();
            if (!positions.contains(pos) && !exterior.contains(pos) &&
                    pos.x() >= -1 && pos.x() <= sx + 1 &&
                    pos.y() >= -1 && pos.y() <= sy + 1 &&
                    pos.z() >= -1 && pos.z() <= sz + 1) {
                exterior.add(pos);
                for (Facing dir : Facing.values()) {
                    queue.add(dir.offset(pos));
                }
            }
        }

        return new Result(calculate(positions, Predicate.not(positions::contains)),
                calculate(positions, exterior::contains));
    }

    public int calculate(Set<Position> positions, Predicate<Position> allowed) {
        return positions.stream().mapToInt((position) -> {
            int count = 0;
            for (Facing facing : Facing.values()) {
                Position offset = facing.offset(position);
                if (allowed.test(offset)) {
                    count++;
                }
            }
            return count;
        }).sum();
    }

}
