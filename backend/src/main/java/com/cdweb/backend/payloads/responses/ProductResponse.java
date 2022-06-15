package com.cdweb.backend.payloads.responses;

import com.cdweb.backend.payloads.requests.AttributeAndVariantsRequest;
import com.cdweb.backend.payloads.requests.ProductCombinationRequest;
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
    private String description;
    private Double originalPrice;
    private int originalQuantity;
    private List<String> imageLinks;
    private List<AttributeAndVariantsResponse> attributeAndVariants;
    private List<ProductCombinationResponse> combinations;
    private String brandName;
    private String categoryName;
    private Date createdDate;
    private Date modifiedDate;
}
