package store.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static store.constant.FilePath.PRODUCT_FILE_PATH;
import static store.exception.ExceptionMessage.DUPLICATE_PRODUCT_ERROR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.Promotions;

class ProductServiceTest {
    private ProductService productService;
    private Promotions promotions;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        promotions = new Promotions();
        promotions.add(new Promotion("MD추천상품", 1, 1, null, null));
        promotions.add(new Promotion("탄산2+1", 2, 1, null, null));
        promotions.add(new Promotion("반짝할인", 1, 1, null, null));
    }

    @Test
    void registerProductFrom_실제_테스트() {
        productService.registerProductFrom(PRODUCT_FILE_PATH.getPath(), promotions);
        Products products = productService.getProducts();
        List<String> productIds = products.get().stream()
                .map(Product::getId)
                .toList();

        assertTrue(productIds.contains("콜라탄산2+1"));
        assertTrue(productIds.contains("콜라"));
        assertTrue(productIds.contains("사이다탄산2+1"));
        assertTrue(productIds.contains("사이다"));
        assertTrue(productIds.contains("오렌지주스MD추천상품"));
        assertTrue(productIds.contains("탄산수탄산2+1"));
        assertTrue(productIds.contains("물"));
        assertTrue(productIds.contains("비타민워터"));
        assertTrue(productIds.contains("감자칩반짝할인"));
        assertTrue(productIds.contains("감자칩"));
        assertTrue(productIds.contains("초코바MD추천상품"));
        assertTrue(productIds.contains("에너지바"));
        assertTrue(productIds.contains("정식도시락"));
        assertTrue(productIds.contains("컵라면MD추천상품"));
        assertTrue(productIds.contains("컵라면"));

        assertEquals(16, products.get().size());
    }

    @Test
    void registerProductFrom_테스트() throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null",
                "사이다,1000,8,탄산2+1",
                "사이다,1000,7,null"
        ));

        productService.registerProductFrom(tempFile, promotions);
        Products result = productService.getProducts();

        assertEquals(4, result.get().size());
    }

    @Test
    void registerProductFrom_중복_예외_테스트() throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,8,탄산2+1"
        ));

        assertThatThrownBy(() -> {
            productService.registerProductFrom(tempFile, promotions);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(DUPLICATE_PRODUCT_ERROR.getMessage());
    }
}