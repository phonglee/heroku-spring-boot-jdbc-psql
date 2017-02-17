package com.tgfashion.repository;

import com.tgfashion.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    @Query("delete from Category where id = ?1")
    void deleteCategoryById(Long id);

    @Query("from Category ca where ca.name = ?1")
    Category findByName(String name);
}
