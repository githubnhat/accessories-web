package com.cdweb.backend.converters;

import com.cdweb.backend.entities.Attributes;
import com.cdweb.backend.entities.Carts;
import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.requests.CartRequest;
import com.cdweb.backend.payloads.responses.AttributeResponse;
import com.cdweb.backend.payloads.responses.CartResponse;
import org.springframework.stereotype.Component;

@Component
public class CartConverter {
//    public Carts toEntity(CartRequest request){
//        return Carts.builder()
//                .
//                .build();
//    }


    public CartResponse toResponse(Carts entity){
        return CartResponse.builder()
                .id(entity.getId())
                .discount(entity.getProduct().getDiscount())
                .price(String.valueOf(entity.getProductCombination() == null
                        ? entity.getProduct().getOriginalPrice() :
                        entity.getProductCombination().getPrice()))
                .productName(entity.getProduct().getProductName())
                .productVariantName(entity.getProductCombination().getProductVariantName())
                .quantity(entity.getQuantity())
                .build();
    }
}
