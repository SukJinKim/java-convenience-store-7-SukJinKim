package store.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.ExceptionMessage.PRODUCT_PARSING_FAIL;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductParserTest {

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {
            "콜라,1000,10,탄산2+1",
            "오렌지주스,   1800,9,  MD추천상품",
            "물,500,10,null"
    })
    void parse_정상_테스트(String info) {
        List<String> parsed = ProductParser.parse(info);

        assertEquals(4, parsed.size());
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {
            "콜라,1000,10,탄산2+1, dummy",
            "콜라,1000,10",
            ""
    })
    void parse_예외_테스트(String info) {
        assertThatThrownBy(() -> {
            ProductParser.parse(info);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_PARSING_FAIL.getMessage());
    }

}