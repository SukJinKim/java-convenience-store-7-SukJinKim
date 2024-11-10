package store.util;

import static store.exception.ExceptionMessage.INVALID_ORDER_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.model.Order;

public class OrderFactory {
    private static final String VALID_FORMAT = "^\\[(?<productName>[^\\s\\-]+)\\-(?<quantity>\\d+)\\]$";
    private static final Pattern VALID_PATTERN = Pattern.compile(VALID_FORMAT);

    private OrderFactory() {
    }

    public static Order create(String orderInfo) {
        validateFormat(orderInfo);
        Matcher matcher = VALID_PATTERN.matcher(orderInfo);
        if (matcher.matches()) {
            String productName = matcher.group("productName");
            int quantity = Integer.parseInt(matcher.group("quantity"));
            return new Order(productName, quantity);
        } else {
            throw new IllegalArgumentException(INVALID_ORDER_FORMAT.getMessage());
        }
    }

    private static void validateFormat(String orderInfo) {
        if (!VALID_PATTERN.matcher(orderInfo).matches()) {
            throw new IllegalArgumentException(INVALID_ORDER_FORMAT.getMessage());
        }
    }
}
