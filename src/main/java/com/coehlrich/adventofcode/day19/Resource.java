package com.coehlrich.adventofcode.day19;

public enum Resource {
    ORE(5),
    CLAY(4),
    OBSIDIAN(3),
    GEODE(2);

    public final int minutesRequired;

    private Resource(int minutesRequired) {
        this.minutesRequired = minutesRequired;
    }
}
