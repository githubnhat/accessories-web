package com.cdweb.backend.converters;

import com.cdweb.backend.entities.Branches;
import com.cdweb.backend.entities.Categories;
import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.repositories.BranchRepository;
import com.cdweb.backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final CategoryRepository categoryRepository;

    private final BranchRepository branchRepository;

    public Products toEntity(ProductRequest request) {
        return Products.builder()
                .productName(request.getProductName())
                .price(request.getPrice())
                .year(request.getYear())
                .categories(categoryRepository.findByName(request.getCategoryName()))
                .branches(branchRepository.findByName(request.getBranchName()))
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
                .branchName(entity.getBranches().getName())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
