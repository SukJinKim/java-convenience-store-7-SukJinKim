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
            totalQuantityByProductName.merge(item.productName(), item.purchaseQuantity(), Integer::sum);
        }

        return totalQuantityByProductName;
    }

    public Map<String, Integer> calculateTotalPriceByProductName() {
        Map<String, Integer> totalPriceByProductName = new HashMap<>();

        for (ShoppingItem item : shoppingItems) {
            totalPriceByProductName.merge(item.productName(), item.productPrice(), Integer::sum);
        }

        return totalPriceByProductName;
    }

    public Map<String, Integer> calculateTotalFreeQuantityByProductName() {
        Map<String, Integer> totalFreeQuantityByProductName = new HashMap<>();

        for (ShoppingItem item : shoppingItems) {
            if (item.hasFreeQuantity()) {
                totalFreeQuantityByProductName.merge(item.productName(), item.freeQuantity(), Integer::sum);
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
                .mapToInt(ShoppingItem::calculatePurchaseAmount)
                .sum();
    }

    public int calculateEventDiscount() {
        return shoppingItems.stream()
                .filter(ShoppingItem::hasFreeQuantity)
                .mapToInt(ShoppingItem::calculateEventDiscount)
                .sum();
    }

    public int calculateNonPromotionalAmount() {
        List<String> promotionalProductNames = shoppingItems.stream()
                .filter(ShoppingItem::hasFreeQuantity)
                .map(ShoppingItem::productName)
                .toList();

        return shoppingItems.stream()
                .filter(ShoppingItem::hasFreeQuantity)
                .filter(item -> !promotionalProductNames.contains(item.productName()))
                .mapToInt(ShoppingItem::calculatePurchaseAmount)
                .sum();
    }
}
