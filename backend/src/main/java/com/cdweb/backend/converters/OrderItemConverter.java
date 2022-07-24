package com.cdweb.backend.converters;

import com.cdweb.backend.common.Utils;
import com.cdweb.backend.entities.OrderItems;
import com.cdweb.backend.entities.ProductCombinations;
import com.cdweb.backend.payloads.requests.OrderItemRequest;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.requests.ProductCombinationRequest;
import com.cdweb.backend.payloads.responses.OrderItemResponse;
import com.cdweb.backend.payloads.responses.ProductCombinationResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter {
    public OrderItems toEntity(OrderItemRequest request){
        return OrderItems.builder()
                .productId(request.getProductId())
                .price(request.getPrice())
                .productCombination(request.getProductCombination())
                .quantity(request.getQuantity())
                .discount(request.getDiscount())
                .build();
    }

    public OrderItemResponse toResponse(OrderItems entity){
        return OrderItemResponse.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .price(entity.getPrice())
                .productCombination(entity.getProductCombination())
                .quantity(entity.getQuantity())
                .discount(entity.getDiscount())
                .brand(entity.getBrands().getName())
                .category(entity.getCategories().getName())
                .build();
    }

}
