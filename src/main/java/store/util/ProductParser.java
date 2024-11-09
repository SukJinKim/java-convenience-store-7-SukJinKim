package store.util;

import static store.exception.ExceptionMessage.PRODUCT_PARSING_FAIL;

import java.util.Arrays;
import java.util.List;

public class ProductParser {
    private static final String DELIMITER = ",";
    private static final int VALID_SIZE = 4;

    private ProductParser() {
    }

    public static List<String> parse(String productInfo) {
        List<String> parsedInfo = Arrays.stream(productInfo.split(DELIMITER))
                .map(String::trim)
                .toList();
        validateSize(parsedInfo);
        return parsedInfo;
    }

    private static void validateSize(List<String> parsedInfo) {
        if (parsedInfo.size() != VALID_SIZE) {
            throw new IllegalArgumentException(PRODUCT_PARSING_FAIL.getMessage());
        }
    }
}
