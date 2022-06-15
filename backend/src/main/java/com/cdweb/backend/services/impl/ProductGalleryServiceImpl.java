package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.ProductGalleryConverter;
import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.responses.ProductGalleryResponse;
import com.cdweb.backend.repositories.ProductGalleryRepository;
import com.cdweb.backend.services.IProductGalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductGalleryServiceImpl implements IProductGalleryService {
    private final ProductGalleryRepository productGalleryRepository;
    private final ProductGalleryConverter productGalleryConverter;
    @Override
    public List<ProductGalleryResponse> save(Products product, List<String> imageLinks) {
        List<ProductGalleryResponse> response = new ArrayList<>();
        imageLinks.forEach(e -> response
                .add(productGalleryConverter.toResponse(
                        productGalleryRepository.save(
                                ProductGalleries.builder()
                                .imageLink(e)
                                .product(product)
                                .build()))));
        return response;
    }
}
