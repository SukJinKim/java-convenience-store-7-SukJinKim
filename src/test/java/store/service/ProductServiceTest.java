package store.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.ExceptionMessage.DUPLICATE_PRODUCT_ERROR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        promotions.add(new Promotion("탄산2+1", 2, 1, null, null));
        promotions.add(new Promotion("반짝할인", 1, 1, null, null));
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