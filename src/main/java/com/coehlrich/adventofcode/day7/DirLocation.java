package com.coehlrich.adventofcode.day7;

import java.util.Map;

public record DirLocation(String name, Map<String, Location> locations) implements Location {

    @Override
    public long size() {
        return locations.values().stream().mapToLong(Location::size).sum();
    }

    @Override
    public int fewer() {
        int value = locations.values().stream().mapToInt(Location::fewer).sum();
        if (size() < 100000) {
            value += size();
        }
        return value;
    }

    @Override
    public long fewest(long min) {
        long value = locations.values().stream().mapToLong((location) -> location.fewest(min)).min()
                .orElse(Integer.MAX_VALUE);
        long size = size();
        if (size < value && size > min) {
            return size;
        } else {
            return value;
        }
    }

}
