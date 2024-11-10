package store.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

class PromotionTest {
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        LocalDateTime startDate = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 30, 23, 59);
        promotion = new Promotion("탄산2+1", 2, 1, startDate, endDate);
    }

    static List<Arguments> validDateTime = Arrays.asList(
            Arguments.of(LocalDateTime.of(2024, 11, 1, 0, 0)),
            Arguments.of(LocalDateTime.of(2024, 11, 15, 0, 0)),
            Arguments.of(LocalDateTime.of(2024, 11, 30, 23, 59)));

    @ParameterizedTest(name = "{arguments} 입력")
    @FieldSource("validDateTime")
    void isActiveOn_성공_테스트(LocalDateTime dateTime) {
        assertTrue(promotion.isActiveOn(dateTime));
    }

    static List<Arguments> invalidDateTime = Arrays.asList(
            Arguments.of(LocalDateTime.of(2024, 10, 31, 23, 59)),
            Arguments.of(LocalDateTime.of(2024, 12, 1, 0, 0)));

    @ParameterizedTest(name = "{arguments} 입력")
    @FieldSource("invalidDateTime")
    void isActiveOn_실패_테스트(LocalDateTime dateTime) {
        assertFalse(promotion.isActiveOn(dateTime));
    }
}