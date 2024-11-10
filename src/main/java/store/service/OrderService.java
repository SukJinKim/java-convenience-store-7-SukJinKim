package store.service;

import store.model.Orders;
import store.util.OrderFactory;
import store.util.OrderParser;

public class OrderService {
    public OrderService() {
    }

    public Orders createOrders(String userInput) {
        return new Orders(
                OrderParser.parse(userInput)
                .stream()
                .map(OrderFactory::create)
                .toList()
        );
    }
}
