package org.example.customer.controller;

import org.example.library.model.Customer;
import org.example.library.model.Product;
import org.example.library.model.ShoppingCart;
import org.example.library.service.CustomerService;
import org.example.library.service.ProductService;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Cart");
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null || shoppingCart.getCartItem().size() == 0) {
            model.addAttribute("check", "No items in your cart");
            model.addAttribute("totalPrice", 0);
        }
        else {
            model.addAttribute("totalPrice", shoppingCart.getTotalPrice());
        }
        model.addAttribute("shoppingCart", shoppingCart);
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("id") Long productId,
                            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                            Principal principal,
                            HttpServletRequest request,
                            HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }
        Product product = productService.getProductById(productId);
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = shoppingCartService.addItemToCart(product, quantity, customer);
        session.setAttribute("totalItems", cart.getTotalItems());
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping(value = "/update-cart", params = "action=update")
    public String updateCart(@RequestParam("id") Long productId,
                             @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                             Principal principal,
                             Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        Product product = productService.getProductById(productId);
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = shoppingCartService.updateItemInCart(product, quantity, customer);
        model.addAttribute("shoppingCart", cart);
        return "redirect:/cart";
    }

    @PostMapping(value = "/update-cart", params = "action=delete")
    public String deleteCart(@RequestParam("id") Long productId,
                             Principal principal,
                             Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        Product product = productService.getProductById(productId);
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = shoppingCartService.deleteItemFromCart(product, customer);
        model.addAttribute("shoppingCart", cart);
        return "redirect:/cart";
    }
}
