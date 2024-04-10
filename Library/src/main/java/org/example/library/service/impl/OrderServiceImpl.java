package org.example.library.service.impl;

import org.example.library.model.*;
import org.example.library.repository.CartItemRepository;
import org.example.library.repository.OrderDetailRepository;
import org.example.library.repository.OrderRepository;
import org.example.library.repository.ShoppingCartRepository;
import org.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ShoppingCartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public Order saveOrder(ShoppingCart cart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderStatus("Pending");
        order.setCustomer(cart.getCustomer());
        order.setTax(cart.getTotalPrice()*0.08);
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : cart.getCartItem()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setTotalPrice(item.getTotalPrice());
            orderDetail.setUnitPrice(item.getProduct().getCostPrice());
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetailList.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);
        cartRepository.delete(cart);
        deleteShoppingCart(cart);
        return orderRepository.save(order);
    }

    @Override
    public void acceptOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setDeliveryDate(new Date());
        order.setOrderStatus("Shipping");
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
//        orderDetailRepository.deleteAll(order.getOrderDetailList());
        orderRepository.deleteById(id);
    }

    private void deleteShoppingCart(ShoppingCart cart) {
        if(!ObjectUtils.isEmpty(cart) && !ObjectUtils.isEmpty(cart.getCartItem())){
            cartItemRepository.deleteAll(cart.getCartItem());
        }
        cart.getCartItem().clear();
        cart.setTotalItems(0);
        cart.setTotalPrice(0);
        cartRepository.save(cart);
    }


}
