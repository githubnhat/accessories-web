package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Brands;
import com.cdweb.backend.entities.Categories;
import com.cdweb.backend.entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Products, Long> {
    Products findByProductNameAndIsActiveTrue(String productName);
    Products findByIdAndIsActiveTrue(Long productId);
    boolean existsByIdAndIsActiveTrue(Long id);
    Boolean existsByProductNameAndIsActiveTrue(String productName);
    Page<Products> findByIsActiveTrue(Pageable pageable);


    Page<Products> findByIsActiveTrueOrderByCreatedDateDesc(Pageable pageable);
    
    long countByIsActiveTrue();

    int countByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String productName,
                                                                                String description);

//    @Query(value = "select p.* from products p join product_attributes pa on p.id = pa.product_id " +
//            "join product_attribute_variants pav on pa.id = pav.product_attribute_id" +
//            " join variants v on v.id = pav.variant_id " +
//            "where v.id = :variant_id", nativeQuery = true)
//    List<Products> findByVariantIdAndIsActive(@Param("variant_id") Long variantId);

    List<Products> findByCategoriesAndIsActiveTrue(Categories category, Pageable pageable);

    List<Products> findByBrandsAndIsActiveTrue(Brands brand, Pageable pageable);

    Page<Products> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String productName,
            String description,
            Pageable pageable);
}
