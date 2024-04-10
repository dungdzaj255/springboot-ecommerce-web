package org.example.admin.controller;

import org.example.library.dto.ProductDto;
import org.example.library.model.Category;
import org.example.library.model.Product;
import org.example.library.service.CategoryService;
import org.example.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
//        List<ProductDto> productDtos = productService.findAll();
//        model.addAttribute("products", productDtos);
//        model.addAttribute("title", "Manage Product");
        return "redirect:/products/0";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> productDtos = productService.pageProducts(pageNo);
        model.addAttribute("title", "Manage Product");
        model.addAttribute("size", productDtos.getSize());
        model.addAttribute("totalPages", productDtos.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", productDtos);
        return "products";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchProduct(@PathVariable int pageNo, @RequestParam("keyword") String keyword, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> productsDtos = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Search Result");
        model.addAttribute("size", productsDtos.getSize());
        model.addAttribute("totalPages", productsDtos.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", productsDtos);
        model.addAttribute("keyword", keyword);
        return "result-products";
    }

    @GetMapping("/add-product")
    public String addProductForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto, @RequestParam("imageProduct") MultipartFile imageProduct, RedirectAttributes attributes) {
        try {
            productService.save(productDto, imageProduct);
            attributes.addFlashAttribute("success", "Add successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to add!");
        }
        return "redirect:/products";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Update Product");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("product", productDto);
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String updateProductProcess(@PathVariable Long id, @ModelAttribute("product") ProductDto productDto, @RequestParam("imageProduct") MultipartFile imageProduct, RedirectAttributes attributes) {
        try {
            ProductDto productDtoNew = productService.getById(id);
            productDtoNew.setName(productDto.getName());
            productDtoNew.setDescription(productDto.getDescription());
            productDtoNew.setCostPrice(productDto.getCostPrice());
            productDtoNew.setSalePrice(productDto.getSalePrice());
            productDtoNew.setCurrentQuantity(productDto.getCurrentQuantity());
            productDtoNew.setImage(productDto.getImage());
            productDtoNew.setCategory(productDto.getCategory());
            productDtoNew.set_deleted(productDto.is_deleted());
            productDtoNew.set_activated(productDto.is_activated());
            productService.update(productDtoNew, imageProduct);
            attributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to add!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteProduct(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to deleted");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enableProduct(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to enabled");
        }
        return "redirect:/products";
    }
}
