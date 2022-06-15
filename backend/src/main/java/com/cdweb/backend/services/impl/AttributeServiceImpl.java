package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.AttributeConverter;
import com.cdweb.backend.entities.Attributes;
import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.responses.AttributeResponse;
import com.cdweb.backend.repositories.AttributeRepository;
import com.cdweb.backend.services.IAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttributeServiceImpl implements IAttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeConverter attributeConverter;

    @Override
    public AttributeResponse save(AttributeRequest request) {
        Attributes entity = attributeRepository.findByAttributeNameAndIsActiveTrue(request.getAttributeName());
        if (entity == null) {
            Attributes newEntity = attributeConverter.toEntity(request);
            newEntity.setActive(true);
            Attributes savedEntity = attributeRepository.save(newEntity);
            return attributeConverter.toResponse(savedEntity);
        }
        return null;
    }

    @Override
    public List<AttributeResponse> saveListAttribute(List<String> attributeNames) {
        List<AttributeResponse> response = new ArrayList<>();
        attributeNames.forEach(attributeName -> {
            Attributes entity = attributeRepository.findByAttributeNameAndIsActiveTrue(attributeName);
            if (entity == null) {
                Attributes newEntity = Attributes.builder()
                        .attributeName(attributeName)
                        .isActive(true)
                        .build();
                Attributes savedEntity = attributeRepository.save(newEntity);
                 response.add(attributeConverter.toResponse(savedEntity));
            }
        }
        );
        return response;
    }


    @Override
    public AttributeResponse update(AttributeRequest request, Long id) {
        Attributes entity = attributeRepository.findByAttributeNameAndIsActiveTrue(request.getAttributeName());
        Attributes oldEntity = attributeRepository.findByIdAndIsActiveTrue(id);
        if (entity == null && oldEntity != null) {
            oldEntity.setAttributeName(request.getAttributeName());
            attributeRepository.save(oldEntity);
            return attributeConverter.toResponse(oldEntity);
        }
        return null;
    }

    @Override
    public List<AttributeResponse> findByIsActiveTrue() {
        return attributeRepository.findByIsActiveTrue().stream().map(attributeConverter::toResponse).collect(Collectors.toList());
    }

    @Override
    public Attributes findByAttributeNameAndIsActiveTrue(String attributeName) {
        return attributeRepository.findByAttributeNameAndIsActiveTrue(attributeName);
    }
}
