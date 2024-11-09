package store.util;

import static store.exception.ExceptionMessage.PROMOTION_FILE_READING_FAIL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class PromotionReader {

    private PromotionReader() {
    }

    public static List<String> read(Path path) {
        List<String> promotionInfo;
        try (Stream<String> lines = Files.lines(path)) {
            promotionInfo = lines.skip(1).toList();
        } catch (IOException e) {
            throw new RuntimeException(PROMOTION_FILE_READING_FAIL.getMessage());
        }
        return promotionInfo;
    }
}
