package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.ProductCombinations;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.responses.ProductCombinationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProductCombinationRepository extends JpaRepository<ProductCombinations, Long> {
    List<ProductCombinations> findByProductAndIsActiveTrue(Products product);

    @Query(value = "SELECT min(price) FROM product_combinations " +
            "WHERE product_combinations.product_id = :product_id and product_combinations.is_active = true", nativeQuery = true)
    Double min(@Param("product_id") Long productId);

    @Query(value = "SELECT max(price) FROM product_combinations " +
            "WHERE product_combinations.product_id = :product_id and product_combinations.is_active = true", nativeQuery = true)
    Double max(@Param("product_id") Long productId);

    @Query(value = "SELECT product_combinations.* FROM product_combinations " +
            "WHERE product_combinations.product_id = :product_id " +
            "and product_combinations.unique_string_id = :unique_string_id " +
            "and product_combinations.is_active = true", nativeQuery = true)
    ProductCombinations findByProductIdAndUniqueStringId(@Param("product_id") Long productId, @Param("unique_string_id") String uniqueStringId);

    @Query(value = "SELECT product_combinations.* FROM product_combinations " +
            "WHERE product_combinations.product_id = :product_id " +
            "and product_combinations.is_active = true", nativeQuery = true)
    ProductCombinations findByProductId(@Param("product_id") Long productId);


//    @Query(value =
//            "SELECT pc.* " +
//            "FROM product_combinations as pc " +
//            "WHERE " +
//                "pc.product_id = :product_id " +
//                "and pc.product_variant_name = :p_var_name " +
//                "and pc.is_active = true",
//            nativeQuery = true)
//    ProductCombinations findByProductIdAndProductVariantName(@Param("product_id") Long id, @Param("p_var_name") String productVariantName);


    @Query(value = "SELECT product_combinations.* FROM product_combinations " +
            "WHERE product_combinations.product_id in (select p.id from products p join product_attributes pa on p.id = pa.product_id " +
            "join product_attribute_variants pav on pa.id = pav.product_attribute_id" +
            " join variants v on v.id = pav.variant_id " +
            "where v.id = :variant_id and pa.attribute_id = :attribute_id) and product_combinations.product_variant_name like %:variant_name%" +
            " and product_combinations.is_active = true", nativeQuery = true)
    List<ProductCombinations> findByVariantIdAndProductVariantNameAndIsActiveTrue(@Param("variant_id") Long variantId,
                                                                                  @Param("variant_name") String variantName,
                                                                                  @Param("attribute_id") Long attributeId);

    ProductCombinations findByProductAndProductVariantNameIsNull(Products product);

    ProductCombinations findByProductAndProductVariantNameAndIsActiveTrue(Products product, String productCombination);
}

