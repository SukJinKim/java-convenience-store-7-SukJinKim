package store.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static store.exception.ExceptionMessage.INVALID_PRODUCT_FORMAT;
import static store.exception.ExceptionMessage.NO_SUCH_PROMOTION;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import store.model.Product;
import store.model.Promotion;
import store.model.Promotions;

class ProductFactoryTest {
    private Promotions promotions;

    static List<Arguments> productsWithPromotion = Arrays.asList(
            Arguments.of(List.of("콜라", "1000", "10", "탄산2+1")),
            Arguments.of(List.of("사이다", "1000", "8", "반짝할인")));

    static List<Arguments> productsWithOutPromotion = Arrays.asList(
            Arguments.of(List.of("콜라", "1000", "10", "null")),
            Arguments.of(List.of("사이다", "1000", "8", "null")));

    static List<Arguments> invalidFormat = Arrays.asList(
            Arguments.of(List.of("콜라", "", "10", "null")),
            Arguments.of(List.of("사이다", "1000", "EIGHT", "null")));

    static List<Arguments> noSuchPromotion = Arrays.asList(
            Arguments.of(List.of("콜라", "1000", "10", "앗! 콜라 신발보다 싸다!")),
            Arguments.of(List.of("사이다", "1000", "8", "사장님이 미쳤어요!")));

    @BeforeEach
    void setUp() {
        promotions = new Promotions();
        promotions.add(new Promotion("탄산2+1", 2, 1, null, null));
        promotions.add(new Promotion("반짝할인", 1, 1, null, null));
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @FieldSource("productsWithPromotion")
    void create_정상_프로모션있는경우_테스트(List<String> productInfo) {
        Product product = ProductFactory.create(productInfo, promotions);

        assertEquals(productInfo.get(0), product.getName());
        assertEquals(Integer.parseInt(productInfo.get(1)), product.getPrice());
        assertEquals(Integer.parseInt(productInfo.get(2)), product.getQuantity());
        assertNotNull(product.getPromotion());
        assertEquals(promotions.find(productInfo.get(3)), product.getPromotion());
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @FieldSource("productsWithOutPromotion")
    void create_정상_프로모션없는경우_테스트(List<String> productInfo) {
        Product product = ProductFactory.create(productInfo, promotions);

        assertEquals(productInfo.get(0), product.getName());
        assertEquals(Integer.parseInt(productInfo.get(1)), product.getPrice());
        assertEquals(Integer.parseInt(productInfo.get(2)), product.getQuantity());
        assertNull(product.getPromotion());
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @FieldSource("invalidFormat")
    void create_예외_잘못된형식(List<String> productInfo) {
        assertThatThrownBy(() -> {
            ProductFactory.create(productInfo, promotions);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(INVALID_PRODUCT_FORMAT.getMessage());
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @FieldSource("noSuchPromotion")
    void create_예외_존재하지_않는_프로모션(List<String> productInfo) {
        assertThatThrownBy(() -> {
            ProductFactory.create(productInfo, promotions);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessage(NO_SUCH_PROMOTION.getMessage());
    }
}