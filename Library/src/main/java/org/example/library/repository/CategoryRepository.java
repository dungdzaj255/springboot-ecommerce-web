package org.example.library.repository;

import org.example.library.dto.CategoryDto;
import org.example.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /*Admin*/
    @Query("SELECT c FROM Category c WHERE c.is_activated=true AND c.is_deleted=false")
    List<Category> findAllByActivated();


    /*Customer*/
    @Query("select new org.example.library.dto.CategoryDto(c.id, c.name, count(p.category.id)) " +
            "from Category c inner join Product p on p.category.id = c.id " +
            "where c.is_activated=true AND c.is_deleted=false AND p.is_activated=true AND p.is_deleted=false " +
            "group by c.id")
    List<CategoryDto> getCategoryAndProduct();
}
