package com.coehlrich.adventofcode.day22;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

public enum Direction {
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0),
    UP(0, -1);

    public final int x;
    public final int y;

    public static Map<PosDir, ConvertEdge> edges = new HashMap<>();

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction relative(Direction dir) {
        return switch (this) {
            case UP -> {
                yield dir;
            }
            case DOWN -> {
                yield switch (dir) {
                    case UP -> Direction.DOWN;
                    case LEFT -> Direction.RIGHT;
                    case RIGHT -> Direction.LEFT;
                    case DOWN -> Direction.UP;
                };
            }
            case LEFT -> {
                yield switch (dir) {
                    case UP -> Direction.LEFT;
                    case LEFT -> Direction.DOWN;
                    case RIGHT -> Direction.UP;
                    case DOWN -> Direction.RIGHT;
                };
            }
            case RIGHT -> {
                yield switch (dir) {
                    case UP -> Direction.RIGHT;
                    case LEFT -> Direction.UP;
                    case RIGHT -> Direction.DOWN;
                    case DOWN -> Direction.LEFT;
                };
            }
        };
    }

    public static void update(int[][] map) {
        Map<Point, Side> sides = new HashMap<>();
        Map<Point, List<Direction>> directions = new HashMap<>();
        boolean sample = map.length % 50 != 0;
        int sideSize = sample ? 4 : 50;
        int maxX = map[0].length / sideSize;
        int maxY = map.length / sideSize;
        int minX;
        for (minX = 0; map[0][minX * sideSize] == -1; minX++)
            ;

        Point down = new Point(minX, 0);
        sides.put(down, Side.DOWN);
        directions.put(down, List.of());
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(down);

        List<PosDir> edges = new ArrayList<>();

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            for (Direction dir : Direction.values()) {
                Point moved = dir.move(point);
                if (moved.x() < 0 || moved.x() >= maxX || moved.y() < 0 || moved.y() >= maxY
                        || map[moved.y() * sideSize][moved.x() * sideSize] == -1) {
                    edges.add(new PosDir(point, dir));
                } else if (!sides.containsKey(moved)) {
                    List<Direction> current = new ArrayList<>(directions.get(point));
                    current.add(dir);
                    directions.put(moved, current);

                    sides.put(moved, getFromDirections(current).side());
                    queue.add(moved);
                }
            }
        }
        Map<PosDir, PosDir> connected = new HashMap<>();
        Map<PosDir, Point> connectedPoints = new HashMap<>();

        System.out.println(edges);
        for (PosDir edge : edges) {
            Point pos = edge.point();
            Direction direction = edge.direction();
            List<Direction> usedDirections = new ArrayList<>(directions.get(pos));
            usedDirections.add(direction);
            Side other = getFromDirections(usedDirections).side();
            Point otherPos = sides.entrySet().stream().filter((entry) -> entry.getValue() == other)
                    .map(Map.Entry::getKey).findFirst().get();
            connectedPoints.put(edge, otherPos);
            System.out.println(edge + ": " + otherPos);
        }

        for (PosDir edge : edges) {
            Point pos = edge.point();
            Direction direction = edge.direction();
            Point otherPos = connectedPoints.get(edge);
            Direction otherDirection = connectedPoints.entrySet().stream()
                    .filter((entry) -> entry.getValue().equals(pos) && entry.getKey().point().equals(otherPos))
                    .map(Map.Entry::getKey)
                    .map(PosDir::direction)
                    .findFirst().get();
            connected.put(new PosDir(pos, direction), new PosDir(otherPos, otherDirection));

            List<Direction> usedDirections = new ArrayList<>(directions.get(pos));
            usedDirections.add(direction);
            EnumSet<Axis> inverted = getFromDirections(usedDirections).axis();

            List<Direction> otherUsedDirections = new ArrayList<>(directions.get(otherPos));
            otherUsedDirections.add(otherDirection);
            EnumSet<Axis> otherInverted = getFromDirections(otherUsedDirections).axis();

            Point start = new Point(otherPos.x() * sideSize, otherPos.y() * sideSize);
            start = switch (otherDirection) {
                case UP, LEFT -> start;
                case DOWN -> new Point(start.x(), start.y() + sideSize - 1);
                case RIGHT -> new Point(start.x() + sideSize - 1, start.y());
            };

            Side side = sides.get(pos);
            Side otherSide = sides.get(otherPos);

            boolean flip = (side == Side.FORWARD && otherSide == Side.RIGHT)
                    || (side == Side.RIGHT && otherSide == Side.FORWARD)
                    || (side == Side.BACK && otherSide == Side.LEFT)
                    || (side == Side.LEFT && otherSide == Side.BACK);

            if (inverted != null
                    && !inverted.stream().map(Axis::getDirections).flatMap(List::stream).toList().contains(direction)) {
                flip = !flip;
            }

            if (otherInverted != null && !otherInverted.stream().map(Axis::getDirections).flatMap(List::stream).toList()
                    .contains(otherDirection)) {
                flip = !flip;
            }

            boolean finalFlip = flip;
            Point finalStart = start;

            Function<Point, Point> conversion = (point) -> {
                int x = switch (Axis.get(direction)) {
                    case Z -> point.x();
                    case X -> point.y();
                } % sideSize;

                int result = finalFlip ? sideSize - x - 1 : x;
                if (Axis.get(otherDirection) == Axis.X) {
                    return new Point(finalStart.x(), finalStart.y() + result);
                } else {
                    return new Point(finalStart.x() + result, finalStart.y());
                }
            };

            Direction.edges.put(edge, new ConvertEdge(conversion, otherDirection.opposite()));
            System.out.println(edge + ": " + otherPos + " (" + otherDirection + ")");
        }

        System.out.println(sides);
    }

    private static DirectionResult getFromDirections(List<Direction> directions) {
        Side newSide = Side.DOWN;
        EnumSet<Axis> inverted = EnumSet.noneOf(Axis.class);
        Direction up = null;
        List<Side> sides = new ArrayList<>();
        sides.add(newSide);
        for (PeekingIterator<Direction> iterator = Iterators.peekingIterator(directions.iterator()); iterator
                .hasNext();) {
            Direction direction = iterator.next();
            int amount = 1;
            while (iterator.hasNext() && iterator.peek() == direction) {
                amount++;
                iterator.next();
            }
            if (amount >= 2 && !newSide.onHorizontalDirection()) {
                Axis inversion = switch (direction) {
                    case LEFT, RIGHT -> Axis.X;
                    case UP, DOWN -> Axis.Z;
                };

                if (inverted.contains(inversion)) {
                    inverted.remove(inversion);
                } else {
                    inverted.add(inversion);
                }
            }

            if (newSide.onHorizontalDirection()) {
                Direction positive = switch (newSide) {
                    case FORWARD -> Direction.RIGHT;
                    case RIGHT -> Direction.DOWN;
                    case BACK -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                    default -> throw new IllegalArgumentException("Unexpected value: " + newSide);
                };

                if (inverted.stream().map(Axis::getDirections).flatMap(List::stream).toList().contains(positive)) {
                    positive = positive.opposite();
                }

                Direction top = up;

                if (inverted.stream().map(Axis::getDirections).flatMap(List::stream).toList().contains(top)) {
                    top = top.opposite();
                }

                if (top == direction || top.opposite() == direction) {
                    newSide = top == direction ? Side.UP : Side.DOWN;
                    sides.add(newSide);
                    if (top == direction) {
                        Axis inversion = switch (top) {
                            case UP, DOWN -> Axis.Z;
                            case LEFT, RIGHT -> Axis.X;
                        };
                        if (inverted.contains(inversion)) {
                            inverted.remove(inversion);
                        } else {
                            inverted.add(inversion);
                        }
                    }
                } else {
                    int count = positive == direction ? 1 : -1;
                    count *= amount;
                    List<Side> visited = newSide.rotateY(count);
                    sides.addAll(visited);
                    if (newSide == Side.BACK || newSide == Side.FORWARD) {
                        if (visited.stream().filter((side) -> side == Side.BACK || side == Side.FORWARD).count()
                                % 2 == 1) {
                            if (inverted.contains(Axis.Z)) {
                                inverted.remove(Axis.Z);
                            } else {
                                inverted.add(Axis.Z);
                            }
                        }
                    }
                    newSide = visited.get(visited.size() - 1);
                }
            } else {
                List<Side> visited = switch (direction) {
                    case UP -> amount == 1 ? List.of(Side.FORWARD) : newSide.rotateX(-amount);
                    case DOWN -> amount == 1 ? List.of(Side.BACK) : newSide.rotateX(amount);
                    case LEFT -> amount == 1 ? List.of(Side.LEFT) : newSide.rotateZ(-amount);
                    case RIGHT -> amount == 1 ? List.of(Side.RIGHT) : newSide.rotateZ(amount);
                };
                newSide = visited.get(visited.size() - 1);
                sides.addAll(visited);
            }

            if (up == null && newSide.onHorizontalDirection()) {
                up = switch (newSide) {
                    case FORWARD -> Direction.UP;
                    case RIGHT -> Direction.RIGHT;
                    case BACK -> Direction.DOWN;
                    case LEFT -> Direction.LEFT;
                    default -> throw new IllegalArgumentException("Unexpected value: " + newSide);
                };
            } else if (!newSide.onHorizontalDirection()) {
                up = null;
            }

        }
        return new DirectionResult(sides, newSide, inverted);
    }

    public Point move(Point pos) {
        return new Point(pos.x() + x, pos.y() + y);
    }

    public PosDir otherEdge(int[][] map, Point pos, boolean part2, boolean sample) {
        Direction direction = this;
        Point point = pos;
        if (!part2) {
            if (x > 0) {
                point = new Point(0, pos.y());
            } else if (x < 0) {
                point = new Point(map[0].length - 1, pos.y());
            }

            if (y > 0) {
                point = new Point(pos.x(), 0);
            } else if (y < 0) {
                point = new Point(pos.x(), map.length - 1);
            }
        } else {
            int sideSize = sample ? 4 : 50;
            int x = point.x() % sideSize;
            int y = point.y() % sideSize;
            if (!sample) {
                switch (pos.x() / sideSize) {
                    case 0 -> {
                        switch (pos.y() / sideSize) {
                            case 2 -> {
                                switch (this) {
                                    case LEFT -> {
                                        point = new Point(sideSize, sideSize - y - 1);
                                        direction = Direction.RIGHT;
                                    }
                                    case UP -> {
                                        point = new Point(sideSize, 2 * sideSize + x);
                                        direction = Direction.RIGHT;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                            case 3 -> {
                                switch (this) {
                                    case LEFT -> {
                                        point = new Point(sideSize + y, 0);
                                        direction = Direction.DOWN;
                                    }
                                    case DOWN -> {
                                        point = new Point(sideSize * 2, sideSize - x - 1);
                                        direction = Direction.LEFT;
                                    }
                                    case RIGHT -> {
                                        point = new Point(sideSize + y, sideSize * 2);
                                        direction = Direction.UP;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                        }
                    }
                    case 1 -> {
                        switch (pos.y() / sideSize) {
                            case 0 -> {
                                switch (this) {
                                    case UP -> {
                                        point = new Point(0, sideSize * 3 + x);
                                        direction = Direction.RIGHT;
                                    }
                                    case LEFT -> {
                                        point = new Point(0, (sideSize * 2) + (sideSize - y - 1));
                                        direction = Direction.RIGHT;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                            case 1 -> {
                                switch (this) {
                                    case LEFT -> {
                                        point = new Point(y, sideSize * 2);
                                        direction = Direction.DOWN;
                                    }
                                    case RIGHT -> {
                                        point = new Point(sideSize * 2 + y, sideSize - 1);
                                        direction = Direction.UP;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                            case 2 -> {
                                switch (this) {
                                    case RIGHT -> {
                                        point = new Point(sideSize * 2 - 1, sideSize - y - 1);
                                        direction = Direction.LEFT;
                                    }
                                    case DOWN -> {
                                        point = new Point(sideSize - 1, sideSize * 3 + x);
                                        direction = Direction.LEFT;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                        }
                    }
                    case 2 -> {
                        switch (pos.y() / sideSize) {
                            case 0 -> {
                                switch (this) {
                                    case UP -> {
                                        point = new Point(sideSize - x - 1, sideSize * 4 - 1);
                                        direction = Direction.UP;
                                    }
                                    case RIGHT -> {
                                        point = new Point(sideSize * 2 - 1, sideSize * 3 - y - 1);
                                        direction = Direction.LEFT;
                                    }
                                    case DOWN -> {
                                        point = new Point(sideSize * 2 - 1, sideSize + x);
                                        direction = Direction.LEFT;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                        }
                    }
                }
            } else {
//                switch (pos.x() / sideSize) {
//                    case 0 -> {
//                        switch (pos.y() / sideSize) {
//                            case 1 -> {
//                                switch (this) {
//                                    case UP -> {
//                                        point = new Point(sideSize * 3 - x - 1, 0);
//                                        direction = Direction.DOWN;
//                                    }
//                                    case LEFT -> {
//                                        point = new Point(sideSize * 4 - y - 1, sideSize * 3 - 1);
//                                        direction = Direction.UP;
//                                    }
//                                    case DOWN -> {
//                                        point = new Point(sideSize * 3 - x, sideSize * 3 - 1);
//                                        direction = Direction.UP;
//                                    }
//                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
//                                }
//                            }
//                        }
//                    }
//                    case 1 -> {
//                        switch (pos.y() / sideSize) {
//                            case 1 -> {
//                                switch (this) {
//                                    case UP -> {
//                                        point = new Point(sideSize * 2, x);
//                                        direction = Direction.RIGHT;
//                                    }
//                                    case DOWN -> {
//                                        point = new Point(sideSize * 2, sideSize * 3 - x - 1);
//                                        direction = Direction.RIGHT;
//                                    }
//                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
//                                }
//                            }
//                        }
//                    }
//                    case 2 -> {
//                        switch (pos.y() / sideSize) {
//                            case 0 -> {
//                                switch (this) {
//                                    case LEFT -> {
//                                        point = new Point(sideSize * 2 + y, sideSize);
//                                        direction = Direction.DOWN;
//                                    }
//                                    case UP -> {
//                                        point = new Point(sideSize - x - 1, sideSize);
//                                        direction = Direction.DOWN;
//                                    }
//                                    case RIGHT -> {
//                                        point = new Point(sideSize * 4 - 1, sideSize * 3 - y - 1);
//                                        direction = Direction.LEFT;
//                                    }
//                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
//                                }
//                            }
//                            case 1 -> {
//                                switch (this) {
//                                    case RIGHT -> {
//                                        point = new Point(sideSize * 4 - y - 1, sideSize * 2);
//                                        direction = Direction.DOWN;
//                                    }
//                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
//                                }
//                            }
//                            case 2 -> {
//                                switch (this) {
//                                    case LEFT -> {
//                                        point = new Point(sideSize * 2 - y - 1, sideSize * 2 - 1);
//                                        direction = Direction.UP;
//                                    }
//                                    case DOWN -> {
//                                        point = new Point(sideSize - x - 1, sideSize * 2 - 1);
//                                        direction = Direction.UP;
//                                    }
//                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
//                                }
//                            }
//                        }
//                    }
//                    case 3 -> {
//                        switch (pos.y() / sideSize) {
//                            case 2 -> {
//                                switch (this) {
//                                    case UP -> {
//                                        point = new Point(sideSize * 3 - 1, sideSize * 2 - y - 1);
//                                        direction = Direction.LEFT;
//                                    }
//                                    case RIGHT -> {
//                                        point = new Point(sideSize * 3 - 1, sideSize - y - 1);
//                                        direction = Direction.LEFT;
//                                    }
//                                    case DOWN -> {
//                                        point = new Point(0, sideSize * 2 - y - 1);
//                                        direction = Direction.RIGHT;
//                                    }
//                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
//                                }
//                            }
//                        }
//                    }
//                }

                ConvertEdge edge = edges.get(new PosDir(new Point(pos.x() / sideSize, pos.y() / sideSize), direction));
                point = edge.point().apply(pos);
                direction = edge.direction();
            }
        }
        return new PosDir(point, direction);
    }

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case DOWN -> UP;
        };
    }
}
