package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.AttributeConverter;
import com.cdweb.backend.converters.VariantConverter;
import com.cdweb.backend.entities.*;
import com.cdweb.backend.payloads.requests.AttributeAndVariantsRequest;
import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.responses.AttributeAndVariantsResponse;
import com.cdweb.backend.payloads.responses.AttributeResponse;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.repositories.AttributeRepository;
import com.cdweb.backend.repositories.ProductCombinationRepository;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.repositories.VariantRepository;
import com.cdweb.backend.services.IAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttributeServiceImpl implements IAttributeService {
    private final AttributeRepository attributeRepository;
    private final VariantRepository variantRepository;
    private final AttributeConverter attributeConverter;

    private final ProductRepository productRepository;

    private final ProductCombinationRepository productCombinationRepository;

    @Override
    public AttributeAndVariantsResponse save(AttributeAndVariantsRequest request) {
        Attributes entity = attributeRepository.findByAttributeNameAndIsActiveTrue(request.getAttributeName());
        if (entity == null && request.getAttributeName() != null) {
            Attributes newAttrEntity = Attributes.builder().attributeName(request.getAttributeName()).isActive(true)
                    .build();
            Attributes savedAttrEntity = attributeRepository.save(newAttrEntity);
            AttributeAndVariantsResponse response = AttributeAndVariantsResponse.builder()
                    .attributeId(savedAttrEntity.getId())
                    .attributeName(savedAttrEntity.getAttributeName())
                    .build();
            List<String> variantNameResponse = new ArrayList<>();
            List<String> variantNameRequest = request.getVariantNames();
            if (variantNameRequest != null) {
                for (String v : variantNameRequest) {
                    if (v != null) {
                        Variants varEntity = variantRepository.findByVariantNameAndAttributeIdAndIsActiveTrue(v,
                                savedAttrEntity.getId());
                        if (varEntity == null) {
                            Variants newEntity = Variants.builder()
                                    .variantName(v)
                                    .attribute(savedAttrEntity)
                                    .isActive(true)
                                    .build();
                            Variants savedEntity = variantRepository.save(newEntity);
                            variantNameResponse.add(savedEntity.getVariantName());
                        }
                    }
                    // else {
                    // variantNameResponse.add(varEntity.getVariantName());
                    // }
                }
                response.setVariantNames(variantNameResponse);
            } else {
                response.setVariantNames(null);
            }
            return response;
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
            } else {
                response.add(attributeConverter.toResponse(entity));
            }

        });
        return response;
    }

    @Override
    public AttributeAndVariantsResponse update(AttributeAndVariantsRequest request) {
        Attributes entity = attributeRepository.findByIdAndIsActiveTrue(request.getAttributeId());
        List<String> variantNames = new ArrayList<>();
        if (entity != null) {
            if (!entity.getAttributeName().equals(request.getAttributeName())) {
                entity.setAttributeName(request.getAttributeName());
                attributeRepository.save(entity);
            }
            List<Variants> variants = variantRepository.findByAttributeAndIsActiveTrue(entity);

            // add new variant name
            for (String newVariant : request.getVariantNames()) {
                boolean check = true;
                for (Variants v : variants) {
                    if (newVariant.equals(v.getVariantName())) {
                        check = false;
                    }
                }
                if (check) {
                    variantRepository.save(Variants.builder()
                            .attribute(entity)
                            .variantName(newVariant)
                            .isActive(true)
                            .build());
                }
            }
            // delete new variant name
            List<Variants> updatedVariants = variantRepository.findByAttributeAndIsActiveTrue(entity);
            updatedVariants.forEach(e -> {
                variantNames.add(e.getVariantName());
            });

            for (Variants v : updatedVariants) {
                boolean check = false;
                for (String newVariant : request.getVariantNames()) {
                    if (newVariant.equals(v.getVariantName())) {
                        check = true;
                    }
                }
                if (!check) {
                    v.setActive(false);
                    variantRepository.save(v);
                    updatedVariants.remove(v.getVariantName());
                    // +> cần có thêm attrId để xác đinh com nào có attr do vì có thể trùng var nha
                    List<ProductCombinations> productCombinations = productCombinationRepository
                            .findByVariantIdAndProductVariantNameAndIsActiveTrue(v.getId(), v.getVariantName(),
                                    entity.getId());
                    productCombinations.forEach(p -> {
                        p.setActive(false);
                        productCombinationRepository.save(p);
                    });
                }
            }

        }
        return AttributeAndVariantsResponse.builder()
                .attributeName(entity.getAttributeName())
                .variantNames(variantNames)
                .build();

    }

    @Override
    public List<AttributeResponse> findByIsActiveTrue() {
        return attributeRepository.findByIsActiveTrue().stream().map(attributeConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttributeAndVariantsResponse> findAllAttributeAndVariants(Pageable pageable) {
        List<AttributeAndVariantsResponse> response = new ArrayList<>();
        List<Attributes> attrs = attributeRepository.findByIsActiveTrue(pageable).getContent();
        if (attrs.size() > 0) {
            attrs.forEach(a -> {
                if (a.isActive()) {
                    List<Variants> variants = variantRepository.findByAttributeIdAndIsActiveTrue(a.getId());
                    List<String> variantNames = variants.stream().map((Variants::getVariantName))
                            .collect(Collectors.toList());
                    AttributeAndVariantsResponse attrAndVar = AttributeAndVariantsResponse.builder()
                            .attributeId(a.getId())
                            .attributeName(a.getAttributeName())
                            .variantNames(variantNames)
                            .build();
                    response.add(attrAndVar);
                }
            });
        }
        return response;
    }

    @Override
    public List<AttributeAndVariantsResponse> findAllAttributeAndVariants() {
        List<AttributeAndVariantsResponse> response = new ArrayList<>();
        List<Attributes> attrs = attributeRepository.findByIsActiveTrue();
        attrs.forEach(a -> {
            if (a.isActive()) {
                List<Variants> variants = variantRepository.findByAttributeIdAndIsActiveTrue(a.getId());
                List<String> variantNames = variants.stream().map((Variants::getVariantName))
                        .collect(Collectors.toList());
                AttributeAndVariantsResponse attrAndVar = AttributeAndVariantsResponse.builder()
                        .attributeId(a.getId())
                        .attributeName(a.getAttributeName())
                        .variantNames(variantNames)
                        .build();
                response.add(attrAndVar);
            }
        });
        return response;
    }

    @Override
    public Attributes findByAttributeNameAndIsActiveTrue(String attributeName) {
        return attributeRepository.findByAttributeNameAndIsActiveTrue(attributeName);
    }

    @Override
    public List<AttributeResponse> findByProductIdAndIsActive(Long productId) {
        return attributeRepository.findByProductIdAndIsActive(productId)
                .stream().map(attributeConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public int totalItem() {
        return (int) attributeRepository.countByIsActiveTrue();
    }

    @Override
    public boolean delete(Long[] ids) {
        boolean exists = true;
        for (Long id : ids) {
            if (!attributeRepository.existsByIdAndIsActiveTrue(id))
                exists = false;
        }
        if (exists) {
            for (Long id : ids) {
                Attributes entity = attributeRepository.findByIdAndIsActiveTrue(id);
                entity.setActive(false);
                attributeRepository.save(entity);
            }
        }
        return exists;
    }

    @Override
    public Boolean existsByNameAndIsActive(String attributeName) {
        return attributeRepository.existsByAttributeNameAndIsActiveTrue(attributeName);
    }
}
