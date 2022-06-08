package com.cdweb.backend.services;

import com.cdweb.backend.payloads.requests.VariantRequest;
import com.cdweb.backend.payloads.responses.VariantResponse;

import java.util.List;

public interface IVariantService {
    VariantResponse save(VariantRequest request);
    List<VariantResponse> findByAttributeIdAndIsActiveTrue(Long attributeId);
}
