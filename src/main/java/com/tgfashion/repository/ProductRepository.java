package com.tgfashion.repository;

import com.tgfashion.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("from Product pro where pro.id = ?1")
    Product findById(Integer id);

    @Query("from Product pro where pro.name = ?1")
    Product findByName(String name);

    @Modifying
    @Query("delete from Product where id = ?1")
    void deleteProductById(Integer id);

    @Query("from Product pro where pro.category.name = ?1")
    List<Product> findByCategory(String name);

    @Query("from Product pro where status = ?1")
    List<Product> findByStatus(String status);
}
