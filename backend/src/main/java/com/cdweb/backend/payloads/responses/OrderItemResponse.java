package com.cdweb.backend.payloads.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Double price;
    private String productCombination;
    private int quantity;
    private int discount;
    private String brand;
    private String category;
}
