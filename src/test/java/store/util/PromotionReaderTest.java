package store.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.ExceptionMessage.DUPLICATE_PROMOTION_ERROR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class PromotionReaderTest {

    @Test
    void 프로모션_정보_읽기_정상_테스트() throws IOException {
        Path tempFile = Files.createTempFile("promotions", ".md");
        Files.write(tempFile, List.of(
                "name,buy,get,start_date,end_date",
                "탄산2+1,2,1,2024-01-01,2024-12-31",
                "MD추천상품,1,1,2024-01-01,2024-12-31"
        ));

        List<String> result = PromotionReader.read(tempFile);

        assertEquals(2, result.size());
        assertEquals("탄산2+1,2,1,2024-01-01,2024-12-31", result.getFirst());
        assertEquals("MD추천상품,1,1,2024-01-01,2024-12-31", result.getLast());
    }

    @Test
    void 프로모션_정보_읽기_예외_테스트() {
        Path wrongPath = Path.of("wrong/path/promotions.md");

        assertThatThrownBy(() -> {
            PromotionReader.read(wrongPath);
        }).isInstanceOf(RuntimeException.class)
            .hasMessage(DUPLICATE_PROMOTION_ERROR.getMessage());
    }
}