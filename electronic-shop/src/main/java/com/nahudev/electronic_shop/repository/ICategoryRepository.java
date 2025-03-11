package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
