package com.cdweb.backend.services;

import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.responses.ProductGalleryResponse;

import java.util.List;

public interface IProductGalleryService {
    List<ProductGalleryResponse> save(Products product, List<String> imageLinks);
}
