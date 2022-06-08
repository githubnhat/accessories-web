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
        if (entity==null) {
            Attributes oldEntity = attributeRepository.findByIdAndIsActiveTrue(request.getId());
            if (oldEntity == null) {
                Attributes newEntity = attributeConverter.toEntity(request);
                newEntity.setActive(true);
                Attributes savedEntity = attributeRepository.save(newEntity);
                return attributeConverter.toResponse(savedEntity);
            }
            oldEntity.setAttributeName(request.getAttributeName());
            attributeRepository.save(oldEntity);
            return attributeConverter.toResponse(oldEntity);
        }
        return null;
    }

    @Override
    public List<AttributeResponse> findByIsActiveTrue() {
        return attributeRepository.findByIsActiveTrue().stream().map(attributeConverter :: toResponse).collect(Collectors.toList());
    }
}
