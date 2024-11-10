package store.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
