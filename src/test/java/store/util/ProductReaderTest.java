package store.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.ExceptionMessage.PRODUCT_FILE_READING_FAIL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductReaderTest {

    @Test
    void 상품_정보_읽기_정상_테스트() throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null"
        ));

        List<String> result = ProductReader.read(tempFile);

        assertEquals(2, result.size());
        assertEquals("콜라,1000,10,탄산2+1", result.getFirst());
        assertEquals("콜라,1000,10,null", result.getLast());
    }

    @Test
    void 상품_정보_읽기_예외_테스트() {
        Path wrongPath = Path.of("wrong/path/products.md");

        assertThatThrownBy(() -> {
            ProductReader.read(wrongPath);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage(PRODUCT_FILE_READING_FAIL.getMessage());
    }

}