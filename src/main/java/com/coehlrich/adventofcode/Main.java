package com.coehlrich.adventofcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {

    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        ArgumentAcceptingOptionSpec<Integer> dayOption = parser.accepts("day").withRequiredArg().required()
                .ofType(int.class);
        ArgumentAcceptingOptionSpec<String> rawOption = parser.accepts("input-raw").withRequiredArg();
        ArgumentAcceptingOptionSpec<File> fileOption = parser.accepts("input-file").withRequiredArg()
                .ofType(File.class);
        try {
            OptionSet options = parser.parse(args);
            int dayInt = options.valueOf(dayOption);
            String input;
            if (options.has(rawOption) && options.has(fileOption)
                    || !options.has(rawOption) && !options.has(fileOption)) {
                System.out.println("One of either --input-raw or --input-file is required");
                return;
            } else if (options.has(rawOption)) {
                input = options.valueOf(rawOption);
            } else {
                input = Files.readString(options.valueOf(fileOption).toPath());
            }

            Optional<Day> day = ServiceLoader.load(Day.class).stream().map(Provider::get)
                    .filter((dayInstance) -> dayInstance.day() == dayInt)
                    .findFirst();

            if (day.isEmpty()) {
                System.out.println("Day " + day + " is not added yet.");
                return;
            }

            long time = System.currentTimeMillis();
            Result result = day.get().execute(input);
            long timeTaken = System.currentTimeMillis() - time;
            System.out.println("Part 1 answer: " + result.part1());
            System.out.println("Part 2 answer: " + result.part2());
            System.out.println("Executed in " + timeTaken + "ms");
        } catch (OptionException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
