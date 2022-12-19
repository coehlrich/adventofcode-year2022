package com.coehlrich.adventofcode.day19;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public record Blueprint(int id, Map<Resource, Robot> robots) {

    private static final Pattern BLUEPRINT = Pattern.compile(
            "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");

    public static Blueprint parse(String line) {
        Matcher matcher = BLUEPRINT.matcher(line);
        matcher.matches();

        int id = Integer.parseInt(matcher.group(1));
        int oreOre = Integer.parseInt(matcher.group(2));
        int oreClay = Integer.parseInt(matcher.group(3));
        int oreObsidian = Integer.parseInt(matcher.group(4));
        int clayObsidian = Integer.parseInt(matcher.group(5));
        int oreGeode = Integer.parseInt(matcher.group(6));
        int obsidianGeode = Integer.parseInt(matcher.group(7));

        return new Blueprint(id, Map.of(
                Resource.ORE, new Robot(Resource.ORE, new Object2IntOpenHashMap<>(Map.of(Resource.ORE, oreOre))),
                Resource.CLAY, new Robot(Resource.CLAY, new Object2IntOpenHashMap<>(Map.of(Resource.ORE, oreClay))),
                Resource.OBSIDIAN, new Robot(Resource.OBSIDIAN,
                        new Object2IntOpenHashMap<>(Map.of(Resource.ORE, oreObsidian, Resource.CLAY, clayObsidian))),
                Resource.GEODE, new Robot(Resource.GEODE, new Object2IntOpenHashMap<>(
                        Map.of(Resource.ORE, oreGeode, Resource.OBSIDIAN, obsidianGeode)))));
    }

}
