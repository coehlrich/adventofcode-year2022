package com.coehlrich.adventofcode.day10;

import it.unimi.dsi.fastutil.ints.IntList;

public interface InstructionExecution {
    public int execute(int x, IntList args);
}
