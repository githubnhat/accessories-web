package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Products;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface ProductRepository extends JpaRepositoryImplementation<Products, Long> {
    List<Products> findByProductName(String productName);
}
