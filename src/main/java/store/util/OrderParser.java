package store.util;

import java.util.Arrays;
import java.util.List;

public class OrderParser {
    private static final String DELIMITER = ",";

    private OrderParser() {
    }

    public static List<String> parse(String userInput) {
        return Arrays.stream(userInput.split(DELIMITER))
                .map(String::trim)
                .toList();
    }
}
