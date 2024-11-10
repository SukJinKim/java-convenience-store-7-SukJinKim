package store.service;

import static store.exception.ExceptionMessage.DUPLICATE_PRODUCT_ERROR;

import java.nio.file.Path;
import java.util.List;
import store.dto.Inventory;
import store.dto.InventoryItem;
import store.model.Product;
import store.model.Products;
import store.model.Promotions;
import store.util.ProductFactory;
import store.util.ProductParser;
import store.util.PromotionReader;

public class ProductService {
    private final Products products;

    public ProductService() {
        this.products = new Products();
    }

    public Products getProducts() {
        return products;
    }

    public Products registerProductOf(Path path, Promotions promotions) {
        List<String> fileContents = PromotionReader.read(path);
        List<List<String>> parsed = fileContents.stream().map(ProductParser::parse).toList();
        registerProductOf(parsed, promotions);
        return products;
    }

    public Inventory createInventory() {
        Inventory inventory = new Inventory();
        List<InventoryItem> inventoryItems = createInventoryItems();
        inventoryItems.forEach(inventory::add);
        return inventory;
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

    private List<InventoryItem> createInventoryItems() {
        return products.get().stream()
                .filter(Product::availableForSale)
                .map(p -> new InventoryItem(
                        p.getName(),
                        p.getPrice(),
                        p.getQuantity(),
                        p.getPromotionName()
                ))
                .toList();
    }
}
