package com.cdweb.backend.converters;

import com.cdweb.backend.common.Utils;
import com.cdweb.backend.entities.Attributes;
import com.cdweb.backend.entities.Carts;
import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.requests.CartRequest;
import com.cdweb.backend.payloads.responses.AttributeResponse;
import com.cdweb.backend.payloads.responses.CartResponse;
import org.springframework.stereotype.Component;

@Component
public class CartConverter {
    public Carts toEntity(CartRequest request){
        return null;
    }


    public CartResponse toResponse(Carts entity){
//        String price;
//        int quantity;
//        if(!entity.getProductCombination().isActive() && entity.getProduct().getProductAttributes() != null){
//
//        }
        return CartResponse.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .discount(entity.getProduct().getDiscount())
                .price(

                        (!entity.getProductCombination().isActive()
                        || entity.getProductCombination().getQuantity() == 0)
                        ? Utils.formatNumber(entity.getProduct().getOriginalPrice())
                        : Utils.formatNumber(entity.getProductCombination().getPrice())

                )
                .productName(entity.getProduct().getProductName())
                .productVariantName(entity.getProductCombination().getProductVariantName())
                .quantity((!entity.getProductCombination().isActive()
                        || entity.getProductCombination().getQuantity() == 0) ? 0 :
                        Math.min(entity.getQuantity(), entity.getProductCombination().getQuantity()))
                .build();
    }
}
