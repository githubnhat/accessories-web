package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Products, Long> {
    Products findByProductNameAndIsActiveTrue(String productName);
}
