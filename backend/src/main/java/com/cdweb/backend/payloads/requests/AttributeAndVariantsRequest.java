package com.cdweb.backend.payloads.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttributeAndVariantsRequest {
    private String attributeName;
    private List<String> variantNames;
}