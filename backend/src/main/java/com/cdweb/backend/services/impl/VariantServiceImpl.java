package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.VariantConverter;
import com.cdweb.backend.entities.Attributes;
import com.cdweb.backend.entities.Variants;
import com.cdweb.backend.payloads.requests.VariantRequest;
import com.cdweb.backend.payloads.responses.VariantResponse;
import com.cdweb.backend.repositories.AttributeRepository;
import com.cdweb.backend.repositories.VariantRepository;
import com.cdweb.backend.services.IVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements IVariantService {
    private final VariantRepository variantRepository;

    private final VariantConverter variantConverter;

    private final AttributeRepository attributeRepository;

    @Override
    public VariantResponse save(VariantRequest request) {
        Variants oldEntity = variantRepository.findByVariantNameAndIsActiveTrue(request.getVariantName());
        if (oldEntity == null) {
            Variants entity = variantRepository.findByIdAndIsActiveTrue(request.getId());
            if (entity == null) {
                Variants newEntity = variantConverter.toEntity(request);
                Attributes attribute = attributeRepository.findByIdAndIsActiveTrue(request.getAttributeId());
                newEntity.setAttribute(attribute);
                newEntity.setActive(true);
                Variants savedEntity = variantRepository.save(newEntity);
                VariantResponse response = variantConverter.toResponse(savedEntity);
                response.setAttributeId(savedEntity.getAttribute().getId());
                return response;
            } else {
                Attributes attribute = attributeRepository.findById(request.getAttributeId())
                        .orElseThrow(() -> new IllegalArgumentException("Not found attribute!"));
                entity.setVariantName(request.getVariantName());
                entity.setAttribute(attribute);
                Variants updatedEntity = variantRepository.save(entity);
                VariantResponse response = variantConverter.toResponse(updatedEntity);
                response.setAttributeId(updatedEntity.getAttribute().getId());
                return response;
            }
        }
        return null;
    }

    @Override
    public List<VariantResponse> findByAttributeIdAndIsActiveTrue(Long attributeId) {
        List<VariantResponse> response = new ArrayList<>();
        variantRepository.findByAttributeIdAndIsActiveTrue(attributeId)
                .forEach(v -> response.add(
                        VariantResponse.builder()
                                .variantName(v.getVariantName())
                                .id(v.getId())
                                .attributeId(v.getAttribute().getId())
                                .build()));
        return response;
    }
}
