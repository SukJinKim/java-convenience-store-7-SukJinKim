package store.model;

import java.util.Collections;
import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> get() {
        return Collections.unmodifiableList(orders);
    }

    public List<String> getOrderedProductNames() {
        return orders.stream()
                .map(Order::getProductName)
                .toList();
    }
}
