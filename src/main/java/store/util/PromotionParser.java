package store.util;

import static store.exception.ExceptionMessage.PROMOTION_PARSING_FAIL;

import java.util.Arrays;
import java.util.List;

public class PromotionParser {
    private static final String DELIMITER = ",";
    private static final int VALID_SIZE = 5;

    private PromotionParser() {
    }

    public static List<String> parse(String promotionInfo) {
        List<String> parsedInfo = Arrays.stream(promotionInfo.split(DELIMITER))
                .map(String::trim)
                .toList();

        validateSize(parsedInfo);

        return parsedInfo;
    }

    private static void validateSize(List<String> parsedInfo) {
        if (parsedInfo.size() != VALID_SIZE) {
            throw new IllegalArgumentException(PROMOTION_PARSING_FAIL.getMessage());
        }
    }
}
