package store.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private final List<ShoppingItem> shoppingItems;

    public ShoppingCart() {
        this.shoppingItems = new ArrayList<>();
    }

    public void add(ShoppingItem shoppingItem) {
        shoppingItems.add(shoppingItem);
    }

    public List<ShoppingItem> getShoppingItems() {
        return Collections.unmodifiableList(shoppingItems);
    }

    public Map<String, Integer> calculateTotalPurchaseQuantityByProductName() {
        Map<String, Integer> totalQuantityByProductName = new HashMap<>();
        for (ShoppingItem item : shoppingItems) {
            totalQuantityByProductName.merge(
                    item.productName(),
                    item.purchaseQuantity(),
                    Integer::sum
            );
        }
        return totalQuantityByProductName;
    }

    public Map<String, Integer> calculateTotalPriceByProductName() {
        Map<String, Integer> totalPriceByProductName = new HashMap<>();
        for (ShoppingItem item : shoppingItems) {
            totalPriceByProductName.merge(
                    item.productName(),
                    item.productPrice(),
                    Integer::sum
            );
        }
        return totalPriceByProductName;
    }

    public Map<String, Integer> calculateTotalFreeQuantityByProductName() {
        Map<String, Integer> totalFreeQuantityByProductName = new HashMap<>();
        for (ShoppingItem item : shoppingItems) {
            if (item.freeQuantity() > 0) {
                totalFreeQuantityByProductName.merge(
                        item.productName(),
                        item.freeQuantity(),
                        Integer::sum
                );
            }
        }
        return totalFreeQuantityByProductName;
    }

    public int calculateTotalPurchaseQuantity() {
        return shoppingItems.stream()
                .mapToInt(ShoppingItem::purchaseQuantity)
                .sum();
    }

    public int calculateTotalPurchaseAmount() {
        return shoppingItems.stream()
                .mapToInt(item -> item.productPrice() * item.purchaseQuantity())
                .sum();
    }

    public int calculateEventDiscount() {
        return shoppingItems.stream()
                .filter(item -> item.freeQuantity() > 0)
                .mapToInt(item -> item.productPrice() * item.freeQuantity())
                .sum();
    }

    public int calculateNonPromotionalAmount() {
        List<String> promotionalProductNames = shoppingItems.stream()
                .filter(item -> item.freeQuantity() > 0)
                .map(ShoppingItem::productName)
                .toList();
        return shoppingItems.stream()
                .filter(item -> item.freeQuantity() == 0)
                .filter(item -> !promotionalProductNames.contains(item.productName()))
                .mapToInt(item -> item.productPrice() * item.purchaseQuantity())
                .sum();
    }
}
