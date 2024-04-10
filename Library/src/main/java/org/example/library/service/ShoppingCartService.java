package org.example.library.service;

import org.example.library.model.Customer;
import org.example.library.model.Product;
import org.example.library.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(Product product, int quantity, Customer customer);

    ShoppingCart updateItemInCart(Product product, int quantity, Customer customer);

    ShoppingCart deleteItemFromCart(Product product, Customer customer);

    ShoppingCart getById(Long id);
}
