package com.cdweb.backend.services;

import com.cdweb.backend.entities.Brands;
import com.cdweb.backend.payloads.responses.BrandResponse;

import java.util.List;

public interface IBrandService {
    List<BrandResponse> getAll();
}
