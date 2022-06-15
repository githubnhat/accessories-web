package com.cdweb.backend.converters;

import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductCombinationRequest;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.AttributeAndVariantsResponse;
import com.cdweb.backend.payloads.responses.ProductCombinationResponse;
import com.cdweb.backend.payloads.responses.ProductGalleryResponse;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.repositories.BrandRepository;
import com.cdweb.backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    public Products toEntity(ProductRequest request) {
        return Products.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .originalPrice(request.getOriginalPrice())
                .originalQuantity(request.getOriginalQuantity())
                .build();
    }
    public ProductResponse toResponse(Products entity, List<ProductGalleryResponse> productGalleries,
                                      List<AttributeAndVariantsResponse> attrAndVarRs,
                                      List<ProductCombinationResponse> proComRs) {
        List<String> imageLinks = new ArrayList<>();
        productGalleries.forEach(p -> imageLinks.add(p.getImageLink()));
        return ProductResponse.builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .description(entity.getDescription())
                .originalPrice(entity.getOriginalPrice())
                .originalQuantity(entity.getOriginalQuantity())
                .combinations(proComRs)
                .attributeAndVariants(attrAndVarRs)
                .imageLinks(imageLinks)
                .categoryName(entity.getCategories().getName())
                .brandName(entity.getBrands().getName())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
