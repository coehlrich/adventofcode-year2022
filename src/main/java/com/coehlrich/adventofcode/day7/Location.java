package com.coehlrich.adventofcode.day7;

import java.util.Map;

public interface Location {

    public String name();

    public Map<String, Location> locations();

    public long size();

    public int fewer();

    public long fewest(long min);
}
