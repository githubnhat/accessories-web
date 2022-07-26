package com.cdweb.backend.payloads.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortRequest {
    private String sortBy;
    private Boolean sortDesc;
}
