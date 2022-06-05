package com.cdweb.backend.converters;

import com.cdweb.backend.entities.Brands;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandConverter {

    private final BrandRepository brandRepository;

    public Brands toEntity(BrandResponse response){
        return Brands.builder()
                .code(response.getCode())
                .name(response.getName())
                .build();
    }

    public BrandResponse toResponse(Brands entity){
        return BrandResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .createdBy(entity.getCreatedBy())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
