package store.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static store.constant.FilePath.PRODUCT_FILE_PATH;
import static store.exception.ExceptionMessage.DUPLICATE_PRODUCT_ERROR;
import static store.exception.ExceptionMessage.NO_SUCH_PRODUCT;
import static store.exception.ExceptionMessage.OUT_OF_STOCK;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.dto.Inventory;
import store.dto.InventoryItem;
import store.model.Orders;
import store.model.Product;
import store.model.Promotion;
import store.model.Promotions;

class ProductServiceTest {
    private ProductService productService;
    private Promotions promotions;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        promotions = new Promotions();
        promotions.add(new Promotion("MD추천상품", 1, 1,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
        promotions.add(new Promotion("탄산2+1", 2, 1,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)));
        promotions.add(new Promotion("반짝할인", 1, 1,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
        orderService = new OrderService();
    }

    @Test
    void registerProductOf_실제_테스트() {
        productService.registerProductOf(PRODUCT_FILE_PATH.getPath(), promotions);
        Set<Product> products = productService.getProducts();
        List<String> productIds = products.stream()
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

        assertEquals(16, products.size());
    }

    @Test
    void registerProductOf_테스트() throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null",
                "사이다,1000,8,탄산2+1",
                "사이다,1000,7,null"
        ));

        productService.registerProductOf(tempFile, promotions);
        Set<Product> result = productService.getProducts();

        assertEquals(4, result.size());
    }

    @Test
    void registerProductOf_중복_예외_테스트() throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,탄산2+1",
                "사이다,1000,8,탄산2+1"
        ));

        assertThatThrownBy(() -> {
            productService.registerProductOf(tempFile, promotions);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(DUPLICATE_PRODUCT_ERROR.getMessage());
    }

    @Test
    void createInventory_테스트() throws IOException {
        // Given
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,MD추천상품",
                "사이다,1000,8,탄산2+1"
        ));

        productService.registerProductOf(tempFile, promotions);

        // When
        Inventory inventory = productService.createInventory();

        // Then
        assertEquals(1, inventory.getItems().size());

        InventoryItem item = inventory.getItems().getFirst();
        assertEquals("콜라", item.getName());
        assertEquals(1000, item.getPrice());
        assertEquals(10, item.getQuantity());
        assertEquals("MD추천상품", item.getPromotionName());
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {"[콜라-20],[사이다-5]", "[콜라-5],[사이다-15]"})
    void reviewOrders_정상_테스트(String userInput) throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null",
                "사이다,1000,8,탄산2+1",
                "사이다,1000,7,null"
        ));

        productService.registerProductOf(tempFile, promotions);
        Orders orders = orderService.createOrders(userInput);

        assertDoesNotThrow(() -> {
            productService.reviewOrders(orders);
        });
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {"[감자칩-20]", "[제로콜라-5]"})
    void reviewOrders_존재하지_않는_상품_예외_테스트(String userInput) throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null",
                "사이다,1000,8,탄산2+1",
                "사이다,1000,7,null"
        ));

        productService.registerProductOf(tempFile, promotions);
        Orders orders = orderService.createOrders(userInput);

        assertThatThrownBy(() -> {
            productService.reviewOrders(orders);
        }).isInstanceOf(NoSuchElementException.class).hasMessage(NO_SUCH_PRODUCT.getMessage());
    }

    @ParameterizedTest(name = "{arguments} 입력")
    @ValueSource(strings = {"[콜라-21]", "[사이다-16]"})
    void reviewOrders_재고수량_초과_예외_테스트(String userInput) throws IOException {
        Path tempFile = Files.createTempFile("products", ".md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null",
                "사이다,1000,8,탄산2+1",
                "사이다,1000,7,null"
        ));

        productService.registerProductOf(tempFile, promotions);
        Orders orders = orderService.createOrders(userInput);

        assertThatThrownBy(() -> {
            productService.reviewOrders(orders);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(OUT_OF_STOCK.getMessage());
    }
}