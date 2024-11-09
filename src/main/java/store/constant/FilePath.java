package store.constant;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum FilePath {
    PROMOTION_FILE_PATH(String.join(File.separator, "src", "main", "resources", "promotions.md")),
    PRODUCT_FILE_PATH(String.join(File.separator, "src", "main", "resources", "products.md"));

    private String filePath;

    FilePath(String filePath) {
        this.filePath = filePath;
    }

    public Path getPath() {
        return Paths.get(filePath);
    }
}
