package store.model;

public record ShoppingItem(String productId, String productName, int productPrice, int purchaseQuantity, int freeQuantity) {
    public boolean hasFreeQuantity() {
        return freeQuantity > 0;
    }

    public int calculatePurchaseAmount() {
        return productPrice * purchaseQuantity;
    }

    public int calculateEventDiscount() {
        return productPrice * freeQuantity;
    }
}
