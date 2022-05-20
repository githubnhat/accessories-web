package com.cdweb.backend.converters;

import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public Products toEntity(ProductRequest request) {
        return Products.builder()
                .productName(request.getProductName())
                .price(request.getPrice())
                .url(request.getUrl())
                .year(request.getYear())
                .build();
    }
    public ProductResponse toResponse(Products entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .price(entity.getPrice())
                .url(entity.getUrl())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
