package store.service;

import static store.exception.ExceptionMessage.DUPLICATE_PRODUCT_ERROR;

import java.nio.file.Path;
import java.util.List;
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

    public void registerProductFrom(Path path, Promotions promotions) {
        List<String> fileContents = PromotionReader.read(path);
        List<List<String>> parsed = fileContents.stream().map(ProductParser::parse).toList();
        registerProduct(promotions, parsed);
    }

    private void registerProduct(Promotions promotions, List<List<String>> parsed) {
        parsed.forEach(p -> {
            Product product = ProductFactory.create(p, promotions);
            boolean isDuplicated = !products.add(product);
            if (isDuplicated) {
                throw new IllegalArgumentException(DUPLICATE_PRODUCT_ERROR.getMessage());
            }
        });
    }
}
