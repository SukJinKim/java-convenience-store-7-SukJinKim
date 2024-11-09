package store.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.ExceptionMessage.INVALID_PROMOTION_FORMAT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import store.model.Promotion;

class PromotionFactoryTest {

    static List<Arguments> promotionInfos = Arrays.asList(
            Arguments.of(List.of("탄산2+1", "2", "1", "2024-01-01", "2024-12-31")),
            Arguments.of(List.of("MD추천상품", "1", "1", "2024-01-01", "2024-12-31")),
            Arguments.of(List.of("반짝할인", "1", "1", "2024-11-01", "2024-11-30")));


    @ParameterizedTest(name = "{arguments} 입력")
    @FieldSource("promotionInfos")
    void create_정상_테스트(List<String> promotionInfo) {

        Promotion promotion = PromotionFactory.create(promotionInfo);

        assertEquals(promotionInfo.get(0), promotion.getName());
        assertEquals(Integer.parseInt(promotionInfo.get(1)), promotion.getBuy());
        assertEquals(Integer.parseInt(promotionInfo.get(2)), promotion.getGet());
        assertEquals(LocalDate.parse(promotionInfo.get(3)).atStartOfDay(), promotion.getStartDate());
        assertEquals(LocalDate.parse(promotionInfo.get(4)).atTime(LocalTime.MAX), promotion.getEndDate());
    }

    @Test
    void create_숫자형식_예외() {
        List<String> invalidNumberFormat = List.of("탄산2+1", "two", "1", "2024-01-01", "2024-12-31");
        assertThatThrownBy(() -> {
            PromotionFactory.create(invalidNumberFormat);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(INVALID_PROMOTION_FORMAT.getMessage());
    }

    @Test
    void create_date형식_예외() {
        List<String> invalidDateFormat = List.of("탄산2+1", "2", "1", "2024/01/01", "2024/12/31");
        assertThatThrownBy(() -> {
            PromotionFactory.create(invalidDateFormat);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(INVALID_PROMOTION_FORMAT.getMessage());
    }
}