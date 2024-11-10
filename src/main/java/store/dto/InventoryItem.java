package store.dto;

public class InventoryItem {
    private static final String ITEM_IN_STOCK = "- %s %,d원 %,d개 %s";
    private static final String ITEM_OUT_OF_STOCK = "- %s %,d원 재고 없음 %s";

    private final String name;
    private final int price;
    private final int quantity;
    private final String promotionName;

    public InventoryItem(String name, int price, int quantity, String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
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

    public String getPromotionName() {
        return promotionName;
    }

    @Override
    public String toString() {
        if (inStock()) {
            return String.format(ITEM_IN_STOCK, name, price, quantity, promotionName);
        }
        return String.format(ITEM_OUT_OF_STOCK, name, price, promotionName);
    }

    private boolean inStock() {
        return quantity > 0;
    }
}
