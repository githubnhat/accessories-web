package com.cdweb.backend.services;

import com.cdweb.backend.payloads.requests.VariantRequest;
import com.cdweb.backend.payloads.responses.VariantResponse;

import java.util.List;

public interface IVariantService {
    VariantResponse save(VariantRequest request);
    VariantResponse update(VariantRequest request, Long id);
    List<VariantResponse> findByAttributeIdAndIsActiveTrue(Long attributeId);
}
