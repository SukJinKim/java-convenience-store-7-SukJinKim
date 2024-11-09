package store.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Promotions {
    private final Set<Promotion> promotions;

    public Promotions() {
        this.promotions = new HashSet<>();
    }

    public boolean add(Promotion promotion) {
        return promotions.add(promotion);
    }

    public Set<Promotion> get() {
        return Collections.unmodifiableSet(promotions);
    }
}
