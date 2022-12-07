package com.coehlrich.adventofcode.day7;

import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    private static final Pattern CD = Pattern.compile("\\$ cd (.+)");
    private static final Pattern FILE = Pattern.compile("(\\d+) (.+)");
    private static final Pattern DIR = Pattern.compile("dir (.+)");

    @Override
    public Result execute(String input) {
        Stack<DirLocation> currentDirectory = new Stack<>();
        DirLocation root = new DirLocation("/", new HashMap<>());
        currentDirectory.add(root);

        for (String line : input.lines().toList()) {
            Matcher matcher;
            if (line.equals("$ cd ..")) {
                currentDirectory.pop();
            } else if ((matcher = CD.matcher(line)).matches() && !line.equals("$ cd /")) {
                String name = matcher.group(1);
                currentDirectory.push((DirLocation) currentDirectory.peek().locations().get(name));
            } else if ((matcher = FILE.matcher(line)).matches()) {
                int size = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                currentDirectory.peek().locations().put(name, new FileLocation(name, size));
            } else if ((matcher = DIR.matcher(line)).matches()) {
                String name = matcher.group(1);
                currentDirectory.peek().locations().put(name, new DirLocation(name, new HashMap<>()));
            }
        }

        long needed = root.size() - 40000000l;

        return new Result(Integer.toString(root.fewer()), Long.toString(root.fewest(needed)));
    }

}
