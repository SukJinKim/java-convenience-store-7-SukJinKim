package store.controller;

import static store.constant.FilePath.PRODUCT_FILE_PATH;
import static store.constant.FilePath.PROMOTION_FILE_PATH;

import store.dto.Inventory;
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
        while (true) {
            greet();
            Orders orders = takeOrder();
            ShoppingCart cart = addOrdersToCart(orders);
            boolean hasMemberShipDiscount = inputView.askMemberShipDiscount();
            // TODO cart와 멤버십 할인 여부 합해서 Receipt이라는 dto 생성하고 출력하기
            // TODO 재구매 여부 묻기
            break;
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

    private ShoppingCart addOrdersToCart(Orders orders) {
        ShoppingCart cart = new ShoppingCart();

        for (Order order : orders.get()) {
            processPromotionalOrder(cart, order);
            processNonPromotionalOrder(cart, order);
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
            addToCart(order, cart, promotionalProduct, orderedQuantity,
                    promotionalProduct.calcFreeQuantity(orderedQuantity));
        }
        if (stock < orderedQuantity) {
            handleInsufficientPromotionalStock(cart, order, promotionalProduct, stock);
        }
    }

    private boolean exists(Product product) {
        return product != null;
    }

    private void addToCart(Order order, ShoppingCart cart, Product product, int purchaseQuantity, int freeQuantity) {
        order.minusQuantity(purchaseQuantity);
        cart.add(new ShoppingItem(product.getId(), product.getName(), purchaseQuantity, freeQuantity));
    }

    private void handleInsufficientPromotionalStock(ShoppingCart cart, Order order, Product promotionalProduct,
                                                    int stock) {
        int purchaseQuantity = stock;
        int freeQuantity = promotionalProduct.calcFreeQuantity(purchaseQuantity);
        int promotionalQuantity = promotionalProduct.calcPromotionalQuantity(purchaseQuantity);
        int nonPromotionalQuantity = order.getQuantity() - promotionalQuantity;

        boolean response = inputView.notifyNotAvailablePromotion(order.getProductName(), nonPromotionalQuantity);
        if (response) {
            addToCart(order, cart, promotionalProduct, purchaseQuantity, freeQuantity);
        }
        if (!response) {
            addToCart(order, cart, promotionalProduct, promotionalQuantity,
                    promotionalProduct.calcFreeQuantity(promotionalQuantity));
        }
    }

    private int adjustOrderQuantity(Order order, Product promotionalProduct) {
        int orderedQuantity = order.getQuantity();
        if (promotionalProduct.lessOrdered(orderedQuantity) &&
            inputView.notifyLessOrdered(order.getProductName(), promotionalProduct.getGet())) {
            orderedQuantity += promotionalProduct.getGet();
        }
        return orderedQuantity;
    }

    private void processNonPromotionalOrder(ShoppingCart cart, Order order) {
        if (order.getQuantity() <= 0) {
            return;
        }

        Product nonPromotionalProduct = productService.findNonPromotionalProductByName(order.getProductName());
        if (exists(nonPromotionalProduct)) {
            addToCart(order, cart, nonPromotionalProduct, order.getQuantity(), 0);
        }
    }
}
