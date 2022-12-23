package com.coehlrich.adventofcode.day22;

import java.util.Arrays;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        String[] parts = input.split("\n\n");

        int maxWidth = parts[0].lines().mapToInt(String::length).max().getAsInt();

        char[][] showMap = parts[0].lines().map(String::toCharArray).map((array) -> Arrays.copyOf(array, maxWidth))
                .toArray(char[][]::new);

        int[][] map = parts[0].lines().map((line) -> {
            int[] row = new int[maxWidth];
            Arrays.fill(row, -1);
            for (int i = 0; i < line.length(); i++) {
                row[i] = switch (line.charAt(i)) {
                    case ' ' -> -1;
                    case '.' -> 0;
                    case '#' -> 1;
                    default -> throw new IllegalArgumentException("Unexpected value: " + line.charAt(i));
                };
            }
            return row;
        }).toArray(int[][]::new);

        Direction.update(map);

        String instructions = parts[1];
        instructions = instructions.replace("\n", "");

        int part1 = calculate(map, instructions, new char[showMap.length][showMap[0].length], false);
        int part2 = calculate(map, instructions, showMap, true);

//        for (int y = 0; y < showMap.length; y++) {
//            for (int x = 0; x < showMap[y].length; x++) {
//                System.out.print(showMap[y][x]);
//            }
//            System.out.println();
//        }

        return new Result(part1, part2);
    }

    public int calculate(int[][] map, String instructions, char[][] showMap, boolean part2) {
        String movement = "";
        Direction direction = Direction.RIGHT;

        int minX;
        for (minX = 0; map[0][minX] != 0; minX++)
            ;
        Point pos = new Point(minX, 0);

        for (int i = 0; i < instructions.length(); i++) {
            char character = instructions.charAt(i);
            if ('0' <= character && character <= '9') {
                movement += character;
            } else {
                if (!movement.isEmpty()) {
                    PosDir entry = move(map, direction, movement, pos, showMap, part2);
                    pos = entry.point();
                    direction = entry.direction();
                    movement = "";
                }

                int ordinal = direction.ordinal();
                ordinal += switch (character) {
                    case 'L' -> -1;
                    case 'R' -> 1;
                    default -> throw new IllegalArgumentException("Unexpected value: " + character);
                };

                ordinal = ((ordinal % 4) + 4) % 4;
                direction = Direction.values()[ordinal];
            }
        }
        if (!movement.isEmpty()) {
            PosDir entry = move(map, direction, movement, pos, showMap, part2);
            pos = entry.point();
            direction = entry.direction();
        }
        return 1000 * (pos.y() + 1) + 4 * (pos.x() + 1) + direction.ordinal();
    }

    private PosDir move(int[][] map, Direction direction, String movement, Point pos,
            char[][] showMap, boolean part2) {
        int move = Integer.parseInt(movement);
        movement: for (int j = 0; j < move; j++) {
            Point newPos = direction.move(pos);
            Direction newDirection = direction;
            int tile = newPos.y() >= 0 && newPos.y() < map.length && newPos.x() >= 0
                    && newPos.x() < map[0].length ? map[newPos.y()][newPos.x()] : -1;
            switch (tile) {
                case -1 -> {
                    PosDir entry = direction.otherEdge(map, pos, part2, map.length == 12);
                    newPos = entry.point();
                    newDirection = entry.direction();
                    for (; map[newPos.y()][newPos.x()] == -1; newPos = newDirection.move(newPos))
                        ;

                    if (part2) {
                        System.out.println(
                                "Attempting teleport from x=" + pos.x() + ", y=" + pos.y() + ", dir=" + direction
                                        + " to x=" + newPos.x() + ", y=" + newPos.y() + ", dir=" + newDirection);
                    }
                    if (map[newPos.y()][newPos.x()] == 1) {
                        break movement;
                    }

                    if (part2) {
                        System.out.println(
                                "Teleport from x=" + pos.x() + ", y=" + pos.y() + ", dir=" + direction + " to x="
                                        + newPos.x() + ", y=" + newPos.y() + ", dir=" + newDirection);
                    }
                }

                case 1 -> {
                    break movement;
                }
            }
            showMap[pos.y()][pos.x()] = switch (direction) {
                case RIGHT -> '>';
                case DOWN -> 'v';
                case LEFT -> '<';
                case UP -> '^';
            };
            pos = newPos;
            direction = newDirection;
        }
        if (part2) {
            System.out.println("(" + pos.x() + "," + pos.y() + ")");
            System.out.println(direction);
        }
        return new PosDir(pos, direction);
    }

}
