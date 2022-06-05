package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    Categories findByName(String name);
}
