package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Brands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brands, Long> {

    List<Brands> findByIsActiveTrue();

    Brands findByNameAndIsActiveTrue(String branchName);

    Brands findByCodeAndIsActiveTrue(String branchCode);

    boolean existsByNameAndIsActiveTrue(String name);

    boolean existsByCodeAndIsActiveTrue(String code);

    boolean existsByIdAndIsActiveTrue(Long id);

    Brands findByIdAndIsActiveTrue(Long id);
}
