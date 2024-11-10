package store.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    private final List<InventoryItem> inventoryItems;

    public Inventory() {
        this.inventoryItems = new ArrayList<>();
    }

    public boolean add(InventoryItem item) {
        return inventoryItems.add(item);
    }

    public List<InventoryItem> getItems() {
        return Collections.unmodifiableList(inventoryItems);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        inventoryItems.forEach(item -> {
            stringBuilder.append(item.toString()).append(System.lineSeparator());
        });

        return stringBuilder.toString();
    }
}
