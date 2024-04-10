package org.example.library.repository;

import org.example.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /*Admin*/
    @Query("select p from Product p where p.description " +
            "like %?1% or p.name like %?1%")
    List<Product> searchProductList(String keyword);


    /*Customer*/
    @Query("select p from Product p " +
            "where p.is_activated = true and p.is_deleted = false")
    List<Product> getAllProducts();

    @Query(value = "select * from products p " +
            "where p.is_activated = true and p.is_deleted = false " +
            "order by RAND() limit 4", nativeQuery = true)
    List<Product> getRandomProducts();

    @Query("select p from Product p " +
            "inner join Category c on c.id = p.category.id " +
            "where p.category.id = ?1 and p.is_deleted = false and p.is_activated = true")
    List<Product> getProductsByCategory(Long categoryId);

    @Query("select p from Product p " +
            "where p.is_activated = true and p.is_deleted = false " +
            "order by p.costPrice desc")
    List<Product> filterHighPrice();

    @Query("select p from Product p " +
            "where p.is_activated = true and p.is_deleted = false " +
            "order by p.costPrice")
    List<Product> filterLowPrice();
}
