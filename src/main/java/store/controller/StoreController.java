package store.controller;

import static store.constant.FilePath.PRODUCT_FILE_PATH;
import static store.constant.FilePath.PROMOTION_FILE_PATH;

import store.dto.Inventory;
import store.dto.Receipt;
import store.model.Order;
import store.model.Orders;
import store.model.Product;
import store.model.Promotions;
import store.model.ShoppingCart;
import store.model.ShoppingItem;
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
        do {
            greet();
            Orders orders = takeOrder();
            ShoppingCart cart = addOrdersToCart(orders);
            boolean hasMemberShipDiscount = inputView.askMemberShipDiscount();
            outputView.showReceipt(Receipt.of(cart, hasMemberShipDiscount));
            productService.update(cart);
        } while (inputView.askRetry());
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

    private ShoppingCart addOrdersToCart(Orders orders) {
        ShoppingCart cart = new ShoppingCart();

        for (Order order : orders.get()) {
            processPromotionalOrder(cart, order);
            if (order.hasQuantity()) {
                processNonPromotionalOrder(cart, order);
            }
        }

        return cart;
    }

    private void processPromotionalOrder(ShoppingCart cart, Order order) {
        Product promotionalProduct = productService.findPromotionalProductByName(order.getProductName());
        if (!exists(promotionalProduct)) {
            return;
        }
        int orderedQuantity = adjustOrderQuantity(order, promotionalProduct);
        int stock = promotionalProduct.getQuantity();
        if (stock >= orderedQuantity) {
            handleSufficientPromotionalStock(cart, order, promotionalProduct, orderedQuantity);
            return;
        }
        handleInsufficientPromotionalStock(cart, order, promotionalProduct, stock);
    }

    private void handleSufficientPromotionalStock(ShoppingCart cart, Order order,
                                                  Product promotionalProduct, int orderedQuantity) {
        int freeQuantity = promotionalProduct.calcFreeQuantity(orderedQuantity);
        addToCart(order, cart, promotionalProduct, orderedQuantity, freeQuantity);
    }

    private boolean exists(Product product) {
        return product != null;
    }

    private void addToCart(Order order, ShoppingCart cart, Product product, int purchaseQuantity, int freeQuantity) {
        order.reduceQuantity(purchaseQuantity);
        cart.add(new ShoppingItem(product.getId(), product.getName(), product.getPrice(),
                                purchaseQuantity, freeQuantity));
    }

    private void addAdjustedToCart(Order order, ShoppingCart cart,
                                   Product product, int purchaseQuantity, int freeQuantity) {
        order.reduceAllQuantity();
        cart.add(new ShoppingItem(product.getId(), product.getName(), product.getPrice(),
                                purchaseQuantity, freeQuantity));
    }

    private void handleInsufficientPromotionalStock(ShoppingCart cart, Order order,
                                                    Product promotionalProduct, int stock) {
        int promotionalQuantity = promotionalProduct.calcPromotionalQuantity(stock);
        int nonPromotionalQuantity = order.getQuantity() - promotionalQuantity;

        if (inputView.notifyNotAvailablePromotion(order.getProductName(), nonPromotionalQuantity)) {
            addPromotionalItemsToCart(cart, order, promotionalProduct, stock);
            return;
        }

        addAdjustedItemsToCart(cart, order, promotionalProduct, promotionalQuantity);
    }

    private void addPromotionalItemsToCart(ShoppingCart cart, Order order, Product promotionalProduct, int stock) {
        int freeQuantity = promotionalProduct.calcFreeQuantity(stock);

        addToCart(order, cart, promotionalProduct, stock, freeQuantity);
    }

    private void addAdjustedItemsToCart(ShoppingCart cart, Order order,
                                        Product promotionalProduct, int promotionalQuantity) {
        int adjustedFreeQuantity = promotionalProduct.calcFreeQuantity(promotionalQuantity);

        addAdjustedToCart(order, cart, promotionalProduct, promotionalQuantity, adjustedFreeQuantity);
    }

    private int adjustOrderQuantity(Order order, Product promotionalProduct) {
        int orderedQuantity = order.getQuantity();
        int get = promotionalProduct.getGet();

        if (promotionalProduct.lessOrdered(orderedQuantity)) {
            if (inputView.notifyLessOrdered(order.getProductName(), get)) {
                orderedQuantity += get;
            }
        }

        return orderedQuantity;
    }

    private void processNonPromotionalOrder(ShoppingCart cart, Order order) {
        if (order.hasNoQuantity()) {
            return;
        }

        Product nonPromotionalProduct = productService.findNonPromotionalProductByName(order.getProductName());

        if (exists(nonPromotionalProduct)) {
            addToCart(order, cart, nonPromotionalProduct, order.getQuantity(), 0);
        }
    }
}
