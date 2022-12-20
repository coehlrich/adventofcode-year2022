package com.coehlrich.adventofcode.day19;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

public record State(Object2IntMap<Resource> robots, Object2IntMap<Resource> resources, int minutes,
        List<Resource> builds) {

}
