package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Brands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brands, Long> {
    Brands findByName(String name);
    List<Brands> findAll();
}
