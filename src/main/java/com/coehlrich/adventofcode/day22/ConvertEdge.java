package com.coehlrich.adventofcode.day22;

import java.util.function.Function;

public record ConvertEdge(Function<Point, Point> point, Direction direction) {

}
