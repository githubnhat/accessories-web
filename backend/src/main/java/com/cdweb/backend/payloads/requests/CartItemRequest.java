package com.cdweb.backend.payloads.requests;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private Long id;
    private Long productId;
    private int  quantity;
    private String productVariantName;
}
