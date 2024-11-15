package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.Objects;

public class Product {
    private final String id;
    private final String name;
    private final int price;
    private int quantity;
    private final Promotion promotion;

    private Product(String id, String name, int price, int quantity, Promotion promotion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static Product of(String name, int price, int quantity) {
        return Product.of(name, price, quantity, null);
    }

    public static Product of(String name, int price, int quantity, Promotion promotion) {
        return new Product(generateId(name, promotion), name, price, quantity, promotion);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getPromotionName() {
        if (isPromotional()) {
            return promotion.getName();
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public boolean isNotForSale() {
        if (isPromotional()) {
            return !promotion.isActiveOn(DateTimes.now());
        }
        return false;
    }

    public boolean isPromotional() {
        return promotion != null;
    }

    public boolean isNonPromotional() {
        return !isPromotional();
    }

    public int calcFreeQuantity(int purchaseQuantity) {
        int buy = promotion.getBuy();
        int get = promotion.getGet();
        return purchaseQuantity / (buy + get);
    }

    public int calcPromotionalQuantity(int purchaseQuantity) {
        int buy = promotion.getBuy();
        int get = promotion.getGet();
        return (purchaseQuantity / (buy + get)) * (buy + get);
    }

    public boolean lessOrdered(int orderedQuantity) {
        if (!isPromotional()) {
            return false;
        }
        int buy = promotion.getBuy();
        int get = promotion.getGet();
        return hasEnoughStock(buy, get) && (buy == orderedQuantity);
    }

    public int getGet() {
        return promotion.getGet();
    }

    public void reduceStock(int purchaseQuantity) {
        quantity -= purchaseQuantity;
    }

    public boolean hasQuantity() {
        return quantity > 0;
    }

    private boolean hasEnoughStock(int buy, int get) {
        return quantity >= (buy + get);
    }

    private static String generateId(String name, Promotion promotion) {
        if (promotion == null) {
            return name;
        }
        return name + promotion;
    }
}
