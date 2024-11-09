package store.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.ExceptionMessage.DUPLICATE_PROMOTION_ERROR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Promotion;

class PromotionServiceTest {
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionService = new PromotionService();
    }

    @Test
    void register_테스트() throws IOException {
        Path tempFile = Files.createTempFile("promotions", ".md");
        Files.write(tempFile, List.of(
                "name,buy,get,start_date,end_date",
                "탄산2+1,2,1,2024-01-01,2024-12-31",
                "MD추천상품,1,1,2024-01-01,2024-12-31",
                "반짝할인,1,1,2024-11-01,2024-11-30"
        ));

        promotionService.registerPromotionFrom(tempFile);
        Set<Promotion> result = promotionService.getPromotions();

        assertEquals(3, result.size());
    }

    @Test
    void register_중복_예외_테스트() throws IOException {
        Path tempFile = Files.createTempFile("promotions", ".md");
        Files.write(tempFile, List.of(
                "name,buy,get,start_date,end_date",
                "탄산2+1,2,1,2024-01-01,2024-12-31",
                "탄산2+1,2,1,2024-01-01,2024-12-31"
        ));

        assertThatThrownBy(() -> {
            promotionService.registerPromotionFrom(tempFile);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(DUPLICATE_PROMOTION_ERROR.getMessage());
    }
}