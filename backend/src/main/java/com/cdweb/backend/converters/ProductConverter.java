package com.cdweb.backend.converters;

import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductRequest;
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
                .price(request.getPrice())
                .year(request.getYear())
                .categories(categoryRepository.findByName(request.getCategoryName()))
                .brands(brandRepository.findByName(request.getBranchName()))
                .build();
    }
    public ProductResponse toResponse(Products entity, List<ProductGalleries> productGalleries) {
        List<String> imageLinks = new ArrayList<>();
        productGalleries.forEach(p -> imageLinks.add(p.getImageLink()));
        return ProductResponse.builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .year(entity.getYear())
                .price(entity.getPrice())
                .imageLinks(imageLinks)
                .categoryName(entity.getCategories().getName())
                .branchName(entity.getBrands().getName())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
