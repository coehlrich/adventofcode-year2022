package com.coehlrich.adventofcode.day22;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

public enum Direction {
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0),
    UP(0, -1);

    public final int x;
    public final int y;

    public static Map<Map.Entry<Point, Direction>, ConvertEdge> edges = null;

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
                Side side = sides.get(point);
                if (moved.x() < 0 || moved.x() >= maxX || moved.y() < 0 || moved.y() >= maxY
                        || map[moved.y() * sideSize][moved.x() * sideSize] == -1) {
                    edges.add(new PosDir(point, dir));
                } else if (!sides.containsKey(moved)) {
                    List<Direction> current = new ArrayList<>(directions.get(point));
                    current.add(dir);
                    directions.put(moved, current);

                    Side newSide = Side.DOWN;
                    Axis inverted = null;
                    for (PeekingIterator<Direction> iterator = Iterators.peekingIterator(current.iterator()); iterator
                            .hasNext();) {
                        Direction direction = iterator.next();
                        int amount = 1;
                        while (iterator.hasNext() && iterator.peek() == direction) {
                            amount++;
                            iterator.next();
                        }
                        if (amount >= 2 && !newSide.onHorizontalDirection()) {
                            inverted = switch (direction) {
                                case LEFT, RIGHT -> Axis.X;
                                case UP, DOWN -> Axis.Z;
                            };
                        }

                        if (newSide.onHorizontalDirection()) {
                            Direction positive = switch (newSide) {
                                case FORWARD -> Direction.RIGHT;
                                case RIGHT -> Direction.DOWN;
                                case BACK -> Direction.LEFT;
                                case LEFT -> Direction.UP;
                                default -> throw new IllegalArgumentException("Unexpected value: " + newSide);
                            };

                            if (inverted != null && inverted.directions.contains(positive)) {
                                positive = positive.opposite();
                            }

                            int count = positive == direction ? 1 : -1;
                            count *= amount;
                            newSide = newSide.rotateY(count);
                        } else {
                            newSide = switch (direction) {
                                case UP -> amount == 1 ? Side.FORWARD : newSide.rotateX(-amount);
                                case DOWN -> amount == 1 ? Side.BACK : newSide.rotateX(amount);
                                case LEFT -> amount == 1 ? Side.LEFT : newSide.rotateZ(amount);
                                case RIGHT -> amount == 1 ? Side.RIGHT : newSide.rotateZ(-amount);
                            };
                        }
                    }
                    sides.put(moved, newSide);
                    queue.add(moved);
                }
            }
        }

        for (Side side : Stream.of(Side.values()).filter(Side::onHorizontalDirection).toArray(Side[]::new)) {

        }

        System.out.println(sides);
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
                                        point = new Point(x, sideSize * 4 - 1);
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
                switch (pos.x() / sideSize) {
                    case 0 -> {
                        switch (pos.y() / sideSize) {
                            case 1 -> {
                                switch (this) {
                                    case UP -> {
                                        point = new Point(sideSize * 3 - x - 1, 0);
                                        direction = Direction.DOWN;
                                    }
                                    case LEFT -> {
                                        point = new Point(sideSize * 4 - y - 1, sideSize * 3 - 1);
                                        direction = Direction.UP;
                                    }
                                    case DOWN -> {
                                        point = new Point(sideSize * 3 - x, sideSize * 3 - 1);
                                        direction = Direction.UP;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                        }
                    }
                    case 1 -> {
                        switch (pos.y() / sideSize) {
                            case 1 -> {
                                switch (this) {
                                    case UP -> {
                                        point = new Point(sideSize * 2, x);
                                        direction = Direction.RIGHT;
                                    }
                                    case DOWN -> {
                                        point = new Point(sideSize * 2, sideSize * 3 - x - 1);
                                        direction = Direction.RIGHT;
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
                                    case LEFT -> {
                                        point = new Point(sideSize * 2 + y, sideSize);
                                        direction = Direction.DOWN;
                                    }
                                    case UP -> {
                                        point = new Point(sideSize - x - 1, sideSize);
                                        direction = Direction.DOWN;
                                    }
                                    case RIGHT -> {
                                        point = new Point(sideSize * 4 - 1, sideSize * 3 - y - 1);
                                        direction = Direction.LEFT;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                            case 1 -> {
                                switch (this) {
                                    case RIGHT -> {
                                        point = new Point(sideSize * 4 - y - 1, sideSize * 2);
                                        direction = Direction.DOWN;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                            case 2 -> {
                                switch (this) {
                                    case LEFT -> {
                                        point = new Point(sideSize * 2 - y - 1, sideSize * 2 - 1);
                                        direction = Direction.UP;
                                    }
                                    case DOWN -> {
                                        point = new Point(sideSize - x - 1, sideSize * 2 - 1);
                                        direction = Direction.UP;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                        }
                    }
                    case 3 -> {
                        switch (pos.y() / sideSize) {
                            case 2 -> {
                                switch (this) {
                                    case UP -> {
                                        point = new Point(sideSize * 3 - 1, sideSize * 2 - y - 1);
                                        direction = Direction.LEFT;
                                    }
                                    case RIGHT -> {
                                        point = new Point(sideSize * 3 - 1, sideSize - y - 1);
                                        direction = Direction.LEFT;
                                    }
                                    case DOWN -> {
                                        point = new Point(0, sideSize * 2 - y - 1);
                                        direction = Direction.RIGHT;
                                    }
                                    default -> throw new IllegalArgumentException("Unexpected value: " + this);
                                }
                            }
                        }
                    }
                }
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
