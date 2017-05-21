package ru.iskratel.server.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class StringUtils {

    private StringUtils() {
    }

    public static String joinWithIndexByNewLineCharacter(Collection<String> strings) {
        return joinWithIndex(strings, "\n");
    }

    public static String joinWithIndex(Collection<String> strings, String delimiter) {
        int[] index = {0};
        return strings.stream()
                .map(line -> index[0]++ + ". " + line)
                .collect(Collectors.joining(delimiter));
    }
}
