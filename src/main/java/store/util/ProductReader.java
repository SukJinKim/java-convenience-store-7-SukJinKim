package store.util;

import static store.exception.ExceptionMessage.PRODUCT_FILE_READING_FAIL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ProductReader {

    private ProductReader() {
    }

    public static List<String> read(Path path) {
        List<String> productInfo;

        try (Stream<String> lines = Files.lines(path)) {
            productInfo = lines.skip(1).toList();
        } catch (IOException e) {
            throw new RuntimeException(PRODUCT_FILE_READING_FAIL.getMessage());
        }

        return productInfo;
    }
}
