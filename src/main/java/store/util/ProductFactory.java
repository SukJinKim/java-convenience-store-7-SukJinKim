package store.util;

import static store.exception.ExceptionMessage.INVALID_PRODUCT_FORMAT;
import static store.exception.ExceptionMessage.NO_SUCH_PROMOTION;

import java.util.List;
import java.util.NoSuchElementException;
import store.model.Product;
import store.model.Promotions;

public class ProductFactory {
    private static final String NO_PROMOTION = "null";

    private ProductFactory() {
    }

    public static Product create(List<String> productInfo, Promotions promotions) {
        String name = productInfo.get(0);
        int price = parseNumber(productInfo.get(1));
        int quantity = parseNumber(productInfo.get(2));

        if (NO_PROMOTION.equals(productInfo.get(3))) {
            return Product.of(name, price, quantity);
        }

        return createWithPromotion(name, price, quantity, productInfo.get(3), promotions);
    }

    private static int parseNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_PRODUCT_FORMAT.getMessage());
        }
    }

    private static Product createWithPromotion(String name, int price, int quantity,
                                               String promotionName, Promotions promotions) {
        try {
            return Product.of(name, price, quantity, promotions.find(promotionName));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(NO_SUCH_PROMOTION.getMessage());
        }
    }
}
