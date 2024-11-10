package store.controller;

import static store.constant.FilePath.PRODUCT_FILE_PATH;
import static store.constant.FilePath.PROMOTION_FILE_PATH;

import store.dto.Inventory;
import store.model.Orders;
import store.model.Promotions;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private InputView inputView;
    private OutputView outputView;
    private PromotionService promotionService;
    private ProductService productService;
    private OrderService orderService;

    public StoreController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.promotionService = new PromotionService();
        this.productService = new ProductService();
        this.orderService = new OrderService();
    }

    public void run() {
        registerProduct();
        while(true) {
            greet();
            Orders orders = takeOrder();
            // TODO 구매 로직 작성
        }
    }

    private void registerProduct() {
        Promotions promotions = promotionService.registerPromotionFrom(PROMOTION_FILE_PATH.getPath());
        productService.registerProductOf(PRODUCT_FILE_PATH.getPath(), promotions);
    }

    private void greet() {
        outputView.sayHello();
        Inventory inventory = productService.createInventory();
        outputView.showInventory(inventory);
    }

    private Orders takeOrder() {
        while (true) {
            try {
                Orders orders = orderService.createOrders(inputView.requestOrders());
                productService.reviewOrders(orders);
                return orders;
            } catch (Exception e) {
                outputView.displayError(e.getMessage());
            }
        }
    }
}
