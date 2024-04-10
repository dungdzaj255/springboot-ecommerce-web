package org.example.customer.controller;

import org.example.library.dto.CategoryDto;
import org.example.library.model.Category;
import org.example.library.model.Product;
import org.example.library.service.CategoryService;
import org.example.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String products(Model model) {
        List<Product> products = productService.getAllProducts();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("title", "All Products");
        return "products";
    }

    @GetMapping("/product-detail/{id}")
    public String findProductById(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        Long categoryId = product.getCategory().getId();
        List<Product> relateProducts = productService.getProductsByCategory(categoryId);
        model.addAttribute("product", product);
        model.addAttribute("products", relateProducts);
        model.addAttribute("title", product.getName());
        return "product-detail";
    }

    @GetMapping("/products-in-category/{id}")
    public String getProductsByCategory(@PathVariable("id") Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId);
        List<Product> relateProducts = productService.getProductsByCategory(categoryId);
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        model.addAttribute("products", relateProducts);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("title", category.getName());
        return "products";
    }

    @GetMapping("/high-price")
    public String filterHighPrice(Model model) {
        List<Product> products = productService.filterHighPrice();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("title", "High Price");
        return "products";
    }

    @GetMapping("/low-price")
    public String filterLowPrice(Model model) {
        List<Product> products = productService.filterLowPrice();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("title", "Low Price");
        return "products";
    }
}
