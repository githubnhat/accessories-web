package com.cdweb.backend.services;


import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.repositories.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {

    List<ProductResponse> findAll(Pageable pageable);

    int totalItem();

    ProductResponse findById(Long id);

    ProductResponse findByProductName(String productName);

    ProductResponse save(ProductRequest request);

    boolean delete(Long[] ids);

    boolean existsByProductNameAndIsActive(ProductRequest request);

    List<ProductResponse> getArrivalProducts();
}
