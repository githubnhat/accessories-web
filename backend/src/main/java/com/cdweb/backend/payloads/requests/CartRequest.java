package com.cdweb.backend.payloads.requests;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    private Long id;
    private Long productId;
    private int  quantity;
    private String productVariantName;
}
