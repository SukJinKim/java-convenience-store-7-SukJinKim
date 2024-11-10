package store.model;

public class Order {
    private final String productName;
    private final int quantity;

    public Order(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }
}
