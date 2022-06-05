package com.cdweb.backend.payloads.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String productName;
    private int year;
    private Double price;
    private String branchName;
    private String categoryName;
    private List<String> imageLinks;
    private Date createdDate;
    private Date modifiedDate;
}
