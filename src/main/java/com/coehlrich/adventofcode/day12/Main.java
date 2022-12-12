package com.coehlrich.adventofcode.day12;

import java.util.ArrayList;
import java.util.List;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Point start = null;
        Point end = null;
        List<Point> a = new ArrayList<>();
        String[] lines = input.lines().toArray(String[]::new);
        int[][] map = new int[lines.length][];

        for (int j = 0; j < lines.length; j++) {
            String line = lines[j];
            int[] row = new int[line.length()];
            char[] charArray = line.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char character = charArray[i];
                if (character == 'a') {
                    a.add(new Point(i, j));
                }
                int height = switch (character) {
                    case 'S' -> {
                        start = new Point(i, j);
                        a.add(start);
                        yield 0;
                    }
                    case 'E' -> {
                        end = new Point(i, j);
                        yield 26;
                    }
                    default -> character - 'a';
                };
                row[i] = height;
            }
            map[j] = row;
        }

//        for (int y = 0; y < costs.length; y++) {
//            for (int x = 0; x < costs[y].length; x++) {
//                System.out.print(costs[y][x]);
//            }
//            System.out.println();
//        }
        return new Result(calculate(map, start, a, end, false), calculate(map, start, a, end, true));
    }

    public int calculate(int[][] map, Point start, List<Point> a, Point end, boolean part2) {
        int[][] costs = new int[map.length][map[0].length];
        for (int y = 0; y < costs.length; y++) {
            for (int x = 0; x < costs[y].length; x++) {
                costs[y][x] = Integer.MAX_VALUE;
            }
        }
        costs[start.y()][start.x()] = 0;
        if (part2) {
            for (Point point : a) {
                costs[point.y()][point.x()] = 0;
            }
        }

        boolean changes = true;
        while (changes) {
            changes = false;
            for (int y = 0; y < costs.length; y++) {
                for (int x = 0; x < costs[y].length; x++) {
                    int lowest = Integer.MAX_VALUE;
                    int thisTile = map[y][x];
                    if (y > 0 && costs[y - 1][x] != Integer.MAX_VALUE && map[y - 1][x] + 1 >= thisTile) {
                        lowest = Math.min(lowest, costs[y - 1][x] + 1);
                    }

                    if (x > 0 && costs[y][x - 1] != Integer.MAX_VALUE && map[y][x - 1] + 1 >= thisTile) {
                        lowest = Math.min(lowest, costs[y][x - 1] + 1);
                    }

                    if (y < map.length - 1 && costs[y + 1][x] != Integer.MAX_VALUE && map[y + 1][x] + 1 >= thisTile) {
                        lowest = Math.min(lowest, costs[y + 1][x] + 1);
                    }

                    if (x < map[y].length - 1 && costs[y][x + 1] != Integer.MAX_VALUE
                            && map[y][x + 1] + 1 >= thisTile) {
                        lowest = Math.min(lowest, costs[y][x + 1] + 1);
                    }

                    if (lowest < costs[y][x]) {
                        costs[y][x] = lowest;
                        changes = true;
                    }
                }
            }
        }
        return costs[end.y()][end.x()];
    }

}
