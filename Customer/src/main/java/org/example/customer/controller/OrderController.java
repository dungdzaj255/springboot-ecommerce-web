package org.example.customer.controller;

import org.example.library.model.Customer;
import org.example.library.model.Order;
import org.example.library.model.ShoppingCart;
import org.example.library.service.CustomerService;
import org.example.library.service.OrderService;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    ShoppingCartService cartService;

    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal, RedirectAttributes attributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Check out");
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer.getPhoneNumber().trim().isEmpty() || customer.getAddress().trim().isEmpty() || customer.getCity().trim().isEmpty() || customer.getCountry() == null) {
            attributes.addFlashAttribute("error", "You must fill all the information before checking out!");
            return "redirect:/profile";
        }
        model.addAttribute("customer", customer);
        ShoppingCart cart = customer.getShoppingCart();
        model.addAttribute("cart", cart);
        return "checkout";
    }

    @GetMapping("/order")
    public String placeOrder(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Order");
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        List<Order> orders = customer.getOrderList();
        model.addAttribute("orders", orders);
        return "order";
    }

    @PostMapping("/add-order")
    public String saveOrder(@ModelAttribute("shoppingCart") ShoppingCart cart, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        ShoppingCart orderSave = cartService.getById(cart.getId());
        orderService.saveOrder(orderSave);
        return "redirect:/order";
    }

    @GetMapping("/cancel-order")
    public String cancelOrder(Principal principal, @RequestParam("id") Long orderId) {
        if (principal == null) {
            return "redirect:/login";
        }
        orderService.cancelOrder(orderId);
        return "redirect:/order";
    }
}
