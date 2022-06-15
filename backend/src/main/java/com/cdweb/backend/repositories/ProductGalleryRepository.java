package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductGalleryRepository extends JpaRepository<ProductGalleries, Long> {
    List<ProductGalleries> findByProduct(Products product);
}
