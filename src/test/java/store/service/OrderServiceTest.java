package store.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static store.exception.ExceptionMessage.INVALID_ORDER_FORMAT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderServiceTest {
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {"[사이다-2]","[감자칩-1]"})
    void createOrders_정상_테스트(String userInput) {
        assertDoesNotThrow(() -> {
            orderService.createOrders(userInput);
        });
    }


    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {"[사이다  - 2]","[사이다]", "[-1]", "사이다-2", ""})
    void createOrders_예외_테스트(String userInput) {
        assertThatThrownBy(() -> {
            orderService.createOrders(userInput);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(INVALID_ORDER_FORMAT.getMessage());
    }
}