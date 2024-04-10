package org.example.library.service;

import org.example.library.model.Order;
import org.example.library.model.ShoppingCart;

public interface OrderService {
    Order saveOrder(ShoppingCart cart);

    void acceptOrder(Long id);

    void cancelOrder(Long id);
}
