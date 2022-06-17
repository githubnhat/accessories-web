package com.cdweb.backend.services;

import com.cdweb.backend.entities.Attributes;
import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.responses.AttributeAndVariantsResponse;
import com.cdweb.backend.payloads.responses.AttributeResponse;

import java.util.List;

public interface IAttributeService {
    AttributeResponse save(AttributeRequest request);

    List<AttributeResponse> saveListAttribute(List<String> attributeNames);

    AttributeResponse update(AttributeRequest request, Long id);

    List<AttributeResponse> findByIsActiveTrue();

    Attributes findByAttributeNameAndIsActiveTrue(String attributeName);

    List<AttributeAndVariantsResponse> findAllAttributeAndVariants();


}
