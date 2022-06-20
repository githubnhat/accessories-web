package com.cdweb.backend.services;

import com.cdweb.backend.payloads.requests.BrandRequest;
import com.cdweb.backend.payloads.responses.BrandResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBrandService {

    List<BrandResponse> findByIsActiveTrue();

    BrandResponse save(BrandRequest request);

    int totalItem();

    List<BrandResponse> findAll(Pageable pageable);
}
