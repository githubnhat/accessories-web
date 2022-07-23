package com.cdweb.backend.payloads.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long cartId;
    private Long productId;
    private Double price;
    private String productCombination;
    private int quantity;
    private int discount;
}
