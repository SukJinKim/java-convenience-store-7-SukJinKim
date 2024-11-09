package store.util;

import static store.exception.ExceptionMessage.INVALID_PROMOTION_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import store.model.Promotion;

public class PromotionFactory {

    private PromotionFactory() {
    }

    public static Promotion create(List<String> promotionInfo) {
        String name = promotionInfo.get(0);
        int buy = parseNumber(promotionInfo.get(1));
        int get = parseNumber(promotionInfo.get(2));
        LocalDateTime startDate = parseDate(promotionInfo.get(3)).atStartOfDay();
        LocalDateTime endDate = parseDate(promotionInfo.get(4)).atTime(LocalTime.MAX);

        return new Promotion(name, buy, get, startDate, endDate);
    }

    private static int parseNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_PROMOTION_FORMAT.getMessage());
        }
    }

    private static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(INVALID_PROMOTION_FORMAT.getMessage());
        }
    }
}
