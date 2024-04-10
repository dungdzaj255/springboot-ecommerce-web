package org.example.library.service;

import org.example.library.dto.ProductDto;
import org.example.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    /*Admin*/

    List<ProductDto> findAll();
    Product save(ProductDto productDto, MultipartFile imageProduct);
    Product update(ProductDto productDto, MultipartFile imageProduct);
    void deleteById(Long id);
    void enableById(Long id);
    ProductDto getById(Long id);
    Page<ProductDto> pageProducts(int pageNo);
    Page<ProductDto> searchProducts(int pageNo, String keyword);


    /*Customer*/

    List<Product> getAllProducts();
    List<Product> getRandomProducts();
    Product getProductById(Long id);
    List<Product> getProductsByCategory(Long categoryId);
    List<Product> filterHighPrice();
    List<Product> filterLowPrice();
}
