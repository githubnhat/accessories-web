package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    Categories findByNameAndIsActiveTrue(String name);

    List<Categories> findByIsActiveTrue();

    Categories findByCodeAndIsActiveTrue(String code); ///

    Boolean existsByCodeAndIsActiveTrue(String code);
}
