package com.cdweb.backend.services;


import com.cdweb.backend.entities.Products;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    List<Products> findAll();

    List<Products> findAll(Pageable pageable);

    int totalItem();

    Products findById(Long id);

    List<Products> findByProductName(String productName);

    Products save(Products dto);

    boolean delete(Long[] ids);
}
