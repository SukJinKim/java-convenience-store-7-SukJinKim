package store.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.ExceptionMessage.PROMOTION_PARSING_FAIL;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PromotionParserTest {

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {
        "탄산2+1,2,1,2024-01-01,2024-12-31",
        "MD추천상품,1,1,2024-01-01,2024-12-31",
        "반짝할인,1,1,2024-11-01,2024-11-30"
    })
    void parse_정상_테스트(String info) {
        List<String> parsed = PromotionParser.parse(info);

        assertEquals(5, parsed.size());
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {
            "탄산2+1,2,1,2024-01-01,2024-12-31,dummy",
            "MD추천상품,1,1,2024-01-01",
            ""
    })
    void parse_예외_테스트(String info) {
        assertThatThrownBy(() -> {
            PromotionParser.parse(info);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(PROMOTION_PARSING_FAIL.getMessage());
    }
}