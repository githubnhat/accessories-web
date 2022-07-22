package com.cdweb.backend.converters;

import com.cdweb.backend.common.Utils;
import com.cdweb.backend.entities.CartItems;
import com.cdweb.backend.payloads.requests.CartItemRequest;
import com.cdweb.backend.payloads.responses.CartItemResponse;
import org.springframework.stereotype.Component;

@Component
public class CartItemConverter {
    public CartItems toEntity(CartItemRequest request) {
        return null;
    }

    public CartItemResponse toResponse(CartItems entity) {
        int quantity;
        if ((!entity.getProductCombination().isActive()
                || (entity.getProductCombination().getQuantity() == 0
                        && entity.getProductCombination().getProductVariantName() != null))) {
            quantity = 0;
        } else if (entity.getProductCombination().getProductVariantName() == null) {
            quantity = Math.min(entity.getQuantity(), entity.getProduct().getOriginalQuantity());
        } else {
            quantity = Math.min(entity.getQuantity(), entity.getProductCombination().getQuantity());
        }
        return CartItemResponse.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .discount(entity.getProduct().getDiscount())
                .price(

                        (!entity.getProductCombination().isActive()
                                || entity.getProductCombination().getQuantity() == 0)
                                        ? Utils.formatNumber(entity.getProduct().getOriginalPrice())
                                        : Utils.formatNumber(entity.getProductCombination().getPrice()))
                .productName(entity.getProduct().getProductName())
                .productVariantName(entity.getProductCombination().getProductVariantName())
                .quantity(quantity)
                .build();
    }
}
