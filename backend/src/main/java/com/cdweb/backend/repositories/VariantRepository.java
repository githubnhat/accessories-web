package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Variants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface VariantRepository extends JpaRepository<Variants, Long> {

    List<Variants> findByAttributeIdAndIsActiveTrue(Long attributeId);

    Variants findByVariantNameAndIsActiveTrue(String variantName);

    Variants findByIdAndIsActiveTrue(Long id);

    Variants findByVariantNameAndAttributeIdAndIsActiveTrue(String variantName, Long attributeId);
}
