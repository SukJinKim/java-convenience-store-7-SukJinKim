package store.model;

public record ShoppingItem(String productId, String productName, int productPrice, int purchaseQuantity, int freeQuantity) {
}
