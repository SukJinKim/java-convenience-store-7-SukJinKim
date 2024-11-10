package store.model;

public class Order {
    private final String productName;
    private int quantity;

    public Order(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void minusQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
