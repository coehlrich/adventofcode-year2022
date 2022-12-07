package com.coehlrich.adventofcode.day7;

import java.util.HashMap;
import java.util.Map;

public record FileLocation(String name, long size) implements Location {

    @Override
    public Map<String, Location> locations() {
        return new HashMap<>();
    }

    @Override
    public int fewer() {
        return 0;
    }

    @Override
    public long fewest(long min) {
        return Long.MAX_VALUE;
    }

}
