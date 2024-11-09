package store.service;

import static store.exception.ExceptionMessage.DUPLICATE_PROMOTION_ERROR;

import java.util.Set;
import store.model.Promotion;
import store.model.Promotions;
import store.util.PromotionFactory;
import store.util.PromotionParser;
import store.util.PromotionReader;

public class PromotionService {
    private final Promotions promotions;

    public PromotionService(Promotions promotions) {
        this.promotions = promotions;
    }

    public Set<Promotion> getPromotions() {
        return promotions.getPromotions();
    }

    public void register() {
        PromotionReader.read()
                .stream()
                .map(PromotionParser::parse)
                .map(PromotionFactory::create)
                .forEach(promotion -> {
                    boolean isDuplicated = !promotions.add(promotion);
                    if (isDuplicated) {
                        throw new IllegalArgumentException(DUPLICATE_PROMOTION_ERROR.getMessage());
                    }
                });
    }
}
