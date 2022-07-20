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
    private String productVariantName;
    private String price;
    private int discount;
    private int quantity;
}
