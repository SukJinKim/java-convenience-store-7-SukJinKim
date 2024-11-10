package store.controller;

import static store.constant.FilePath.PRODUCT_FILE_PATH;
import static store.constant.FilePath.PROMOTION_FILE_PATH;

import store.model.Products;
import store.model.Promotions;
import store.service.ProductService;
import store.service.PromotionService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private InputView inputView;
    private OutputView outputView;
    private PromotionService promotionService;
    private ProductService productService;

    public StoreController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.promotionService = new PromotionService();
        this.productService = new ProductService();
    }

    public void run() {
        Products products = registerProduct();
        greet();
    }

    private Products registerProduct() {
        Promotions promotions = promotionService.registerPromotionFrom(PROMOTION_FILE_PATH.getPath());
        return productService.registerProductFrom(PRODUCT_FILE_PATH.getPath(), promotions);
    }

    private void greet() {
        outputView.sayHello();
        outputView.showInventory(productService.createInventory());
    }
}
