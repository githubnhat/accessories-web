package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.BrandConverter;
import com.cdweb.backend.entities.Brands;
import com.cdweb.backend.payloads.requests.BrandRequest;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.repositories.BrandRepository;
import com.cdweb.backend.services.IBrandService;
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
public class BrandServiceImpl implements IBrandService {
    private final BrandRepository brandRepository;
    private final BrandConverter brandConverter;

    @Override
    public List<BrandResponse> getAll() {
        List<Brands> response = brandRepository.findAll();
        List<BrandResponse> rs = new ArrayList<>();
        for (Brands b : response)
            rs.add(brandConverter.toResponse(b));
        return rs;
    }

    @Override
    public BrandResponse save(BrandRequest request) {
        BrandResponse response = null;
        // id null mean insert, otherwise mean update
        if(request.getId() == null) {
            Brands brand = brandRepository.findByCode(request.getCode());
            if (brand == null) {
                Brands entity = brandRepository.save(brandConverter.toEntity(request));
                response =  brandConverter.toResponse(entity);
            } return response;
        } else {
            Brands newEntity = brandConverter.toEntity(request);
            newEntity.setId(request.getId());
            Brands entity = brandRepository.save(newEntity);
            return brandConverter.toResponse(entity);
        }
    }

    @Override
    public int totalItem() {
        return (int) brandRepository.count();
    }

    @Override
    public List<BrandResponse> findAll(Pageable pageable) {
        List<BrandResponse> responses = brandRepository.findAll(pageable)
                .stream().map(brandConverter :: toResponse)
                .collect(Collectors.toList());
        return responses;
    }
}
