package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Variants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<Variants, Long> {

    List<Variants> findByAttributeIdAndIsActiveTrue(Long attributeId);

    Variants findByVariantNameAndIsActiveTrue(String variantName);

    Variants findByIdAndIsActiveTrue(Long id);
}
