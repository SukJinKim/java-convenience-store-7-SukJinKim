package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Promotion;
import store.model.Promotions;

class PromotionServiceTest {
    private Promotions promotions;
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotions = new Promotions();
        promotionService = new PromotionService(promotions);
    }

    @Test
    void register_테스트() {
        promotionService.register();

        Set<Promotion> result = promotionService.getPromotions();

        assertEquals(3, result.size());
    }
}