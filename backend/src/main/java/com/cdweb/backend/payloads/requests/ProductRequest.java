package com.cdweb.backend.payloads.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private Long id;
    private String productName;
    private int year;
    private Double price;
    private String url;
}
