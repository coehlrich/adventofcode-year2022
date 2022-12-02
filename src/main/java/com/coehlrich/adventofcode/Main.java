package com.coehlrich.adventofcode;

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
        ArgumentAcceptingOptionSpec<ImportType> importType = parser.accepts("input-type").withRequiredArg().required()
                .withValuesConvertedBy(new CaseInsensitiveEnumArg<>(ImportType.class));
        ArgumentAcceptingOptionSpec<String> inputOption = parser.accepts("input").withRequiredArg().required();
        try {
            OptionSet options = parser.parse(args);
            int dayInt = options.valueOf(dayOption);
            String input = options.valueOf(importType).getInput(dayInt, options.valueOf(inputOption));

            Optional<Day> day = ServiceLoader.load(Day.class).stream().map(Provider::get)
                    .filter((dayInstance) -> dayInstance.day() == dayInt)
                    .findFirst();

            if (day.isEmpty()) {
                System.out.println("Day " + dayInt + " is not added yet.");
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
