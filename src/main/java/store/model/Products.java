package store.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class Products {
    private final Set<Product> products;

    public Products() {
        this.products = new HashSet<>();
    }

    public boolean add(Product product) {
        return products.add(product);
    }

    public void removeIf(Predicate<Product> predicate) {
        products.removeIf(predicate);
    }

    public Set<Product> get() {
        return Collections.unmodifiableSet(products);
    }
}
