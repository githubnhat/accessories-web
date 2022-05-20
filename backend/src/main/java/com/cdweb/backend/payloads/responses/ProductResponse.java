package com.cdweb.backend.payloads.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String productName;
    private int year;
    private Double price;
    private String url;
    private Date createdDate;
    private Date modifiedDate;
}
