package com.coehlrich.adventofcode.day19;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

public record Robot(Resource resource, Object2IntMap<Resource> costs) {

}
