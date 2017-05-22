package ru.iskratel.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    public static Collection<String> readAllLines(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Collection<String> result = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
//                String line = reader.readLine();
//                if (line == null)
//                    break;
                result.add(line);
            }
            return result;
        }
    }
}
