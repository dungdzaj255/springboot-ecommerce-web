package org.example.customer.controller;

import org.example.library.dto.ProductDto;
import org.example.library.model.Category;
import org.example.library.model.Customer;
import org.example.library.model.Product;
import org.example.library.model.ShoppingCart;
import org.example.library.service.CategoryService;
import org.example.library.service.CustomerService;
import org.example.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = {"/", "/index"})
    public String home(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "Home");
        if(principal != null) {
            session.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getShoppingCart();
            if (cart != null) {
                session.setAttribute("totalItems", cart.getTotalItems());
            }
        } else {
            session.setAttribute("username", null);
        }
        return "home";
    }

    @GetMapping("/menu")
    public String index(Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        List<Product> products = productService.getAllProducts();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("title", "Menu");
        return "menu";
    }

    @GetMapping("/contact")
    public String contactUs(Model model) {
        model.addAttribute("title", "Contact");
        return "contact-us";
    }


}
