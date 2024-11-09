package store.service;

import static store.exception.ExceptionMessage.DUPLICATE_PROMOTION_ERROR;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import store.model.Promotion;
import store.model.Promotions;
import store.util.PromotionFactory;
import store.util.PromotionParser;
import store.util.PromotionReader;

public class PromotionService {
    private final Promotions promotions;

    public PromotionService() {
        this.promotions = new Promotions();
    }

    public Set<Promotion> getPromotions() {
        return promotions.get();
    }

    public void registerPromotionFrom(Path path) {
        List<String> fileContents = PromotionReader.read(path);
        List<Promotion> preprocessed = preprocess(fileContents);
        preprocessed.forEach(promotion -> {
            boolean isDuplicated = !promotions.add(promotion);
            if (isDuplicated) {
                throw new IllegalArgumentException(DUPLICATE_PROMOTION_ERROR.getMessage());
            }
        });
    }

    private List<Promotion> preprocess(List<String> fileContents) {
        return fileContents.stream()
                .map(PromotionParser::parse)
                .map(PromotionFactory::create)
                .toList();
    }
}
