package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.BrandConverter;
import com.cdweb.backend.entities.Brands;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.repositories.BrandRepository;
import com.cdweb.backend.services.IBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        for(Brands b : response)
            rs.add(brandConverter.toResponse(b));
        return rs;
    }
}
