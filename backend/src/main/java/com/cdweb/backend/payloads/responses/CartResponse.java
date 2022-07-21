package com.cdweb.backend.payloads.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CartResponse{
    private Long id;
    private String productName;
    private Long productId;
    private String productVariantName;
    private String price;
    private String thumbnail;
    private int discount;
    private int quantity;
}
