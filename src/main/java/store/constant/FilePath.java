package store.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum FilePath {
    PROMOTION_FILE_PATH("src/main/resources/promotions.md");

    private String filePath;

    FilePath(String filePath) {
        this.filePath = filePath;
    }

    public Path getPath() {
        return Paths.get(filePath);
    }
}
