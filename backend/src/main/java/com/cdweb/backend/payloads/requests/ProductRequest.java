package com.cdweb.backend.payloads.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequest {
    private Long id;
    private String productName;
    private int year;
    private Double price;
    private List<String> imageLinks;
    private String branchName;
    private String categoryName;
}
