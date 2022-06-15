package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.ProductCombinations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCombinationRepository extends JpaRepository<ProductCombinations, Long> {
}
