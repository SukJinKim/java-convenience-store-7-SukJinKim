package store.service;

import static store.exception.ExceptionMessage.DUPLICATE_PRODUCT_ERROR;
import static store.exception.ExceptionMessage.NO_SUCH_PRODUCT;
import static store.exception.ExceptionMessage.OUT_OF_STOCK;

import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import store.dto.Inventory;
import store.dto.InventoryItem;
import store.model.Order;
import store.model.Orders;
import store.model.Product;
import store.model.Products;
import store.model.Promotions;
import store.model.ShoppingCart;
import store.model.ShoppingItem;
import store.util.ProductFactory;
import store.util.ProductParser;
import store.util.PromotionReader;

public class ProductService {
    private final Products products;

    public ProductService() {
        this.products = new Products();
    }

    public Set<Product> getProducts() {
        return products.get();
    }

    public void registerProductOf(Path path, Promotions promotions) {
        List<String> fileContents = PromotionReader.read(path);
        List<List<String>> parsed = fileContents.stream()
                .map(ProductParser::parse)
                .toList();

        registerProductOf(parsed, promotions);
    }

    public Inventory createInventory() {
        update();

        Inventory inventory = new Inventory();
        List<InventoryItem> inventoryItems = createInventoryItems();
        inventoryItems.forEach(inventory::add);

        return inventory;
    }

    public void reviewOrders(Orders orders) {
        validateProductExistence(orders);
        validateStockAvailability(orders);
    }

    public List<Product> findByName(String productName) {
        return products.get().stream()
                .filter(product -> productName.equals(product.getName()))
                .toList();
    }

    public Product findPromotionalProductByName(String productName) {
        return findByName(productName).stream()
                .filter(Product::isPromotional)
                .filter(Product::hasQuantity)
                .findFirst()
                .orElse(null);
    }

    public Product findNonPromotionalProductByName(String productName) {
        return findByName(productName).stream()
                .filter(Product::isNonPromotional)
                .filter(Product::hasQuantity)
                .findFirst()
                .orElse(null);
    }

    private void registerProductOf(List<List<String>> parsed, Promotions promotions) {
        parsed.forEach(p -> {
            Product product = ProductFactory.create(p, promotions);
            boolean isDuplicated = !products.add(product);
            if (isDuplicated) {
                throw new IllegalArgumentException(DUPLICATE_PRODUCT_ERROR.getMessage());
            }
        });
    }

    public void update(ShoppingCart cart) {
        for (ShoppingItem item : cart.getShoppingItems()) {
            String productId = item.productId();
            int purchaseQuantity = item.purchaseQuantity();

            products.get().stream()
                    .filter(product -> product.getId().equals(productId))
                    .findFirst()
                    .ifPresent(product -> product.reduceStock(purchaseQuantity));
        }
    }

    private List<InventoryItem> createInventoryItems() {
        return products.get().stream()
                .map(p -> new InventoryItem(p.getName(), p.getPrice(), p.getQuantity(), p.getPromotionName()))
                .toList();
    }

    private void update() {
        products.removeIf(Product::isNotForSale);
    }

    private void validateProductExistence(Orders orders) {
        Set<String> actualProductNames = getProductNames();

        orders.get().stream()
                .map(Order::getProductName)
                .filter(productName -> !actualProductNames.contains(productName))
                .findFirst()
                .ifPresent(invalidName -> {
                    throw new NoSuchElementException(NO_SUCH_PRODUCT.getMessage());
                });
    }

    private Set<String> getProductNames() {
        return products.get().stream()
                .map(Product::getName)
                .collect(Collectors.toSet());
    }

    private void validateStockAvailability(Orders orders) {
        for (Order order : orders.get()) {
            String orderedProductName = order.getProductName();
            int orderedQuantity = order.getQuantity();
            int stockQuantity = getStockQuantity(orderedProductName);

            if (orderedQuantity > stockQuantity) {
                throw new IllegalArgumentException(OUT_OF_STOCK.getMessage());
            }
        }
    }

    private int getStockQuantity(String orderedProductName) {
        return products.get().stream()
                .filter(product -> product.getName().equals(orderedProductName))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
