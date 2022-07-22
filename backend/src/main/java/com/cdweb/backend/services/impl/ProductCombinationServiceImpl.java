package com.cdweb.backend.services.impl;

import com.cdweb.backend.common.Utils;
import com.cdweb.backend.converters.ProductCombinationConverter;
import com.cdweb.backend.entities.ProductCombinations;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductCombinationRequest;
import com.cdweb.backend.payloads.responses.ProductCombinationResponse;
import com.cdweb.backend.repositories.ProductCombinationRepository;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.services.IProductCombinationService;
import com.cdweb.backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCombinationServiceImpl implements IProductCombinationService {
    private final ProductCombinationRepository productCombinationRepository;
    private final ProductCombinationConverter productCombinationConverter;

    private final ProductRepository productRepository;



    @Override
    public List<ProductCombinationResponse> saveListCombinations(Products product, List<ProductCombinationRequest> request) {
        List<ProductCombinationResponse> response = new ArrayList<>();
        if (request.size() > 0) {
            request.forEach(r ->{
                ProductCombinations newEntity = productCombinationConverter.toEntity(r);
                newEntity.setUniqueStringId(Utils.getUniqueStringId(r.getProductVariantName()));
                newEntity.setProduct(product);
                if (r.getIsUse()){
                    newEntity.setActive(true);
                }
                response.add(productCombinationConverter.toResponse(productCombinationRepository.save(newEntity)));
            });
        } else {
            ProductCombinations newEntity = ProductCombinations.builder()
                    .productVariantName(null)
                    .quantity(0)
                    .price(0.0)
                    .uniqueStringId(null)
                    .product(product)
                    .isActive(true)
                    .build();
            response.add(productCombinationConverter.toResponse(productCombinationRepository.save(newEntity)));
        }
        return response;
    }

    @Override
    public List<ProductCombinationResponse> findByProductAndIsActiveTrue(Products product) {
        return productCombinationRepository.findByProductAndIsActiveTrue(product)
                .stream().map(productCombinationConverter :: toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Double minPrice(Long productId) {
        return productCombinationRepository.min(productId);
    }

    @Override
    public Double maxPrice(Long productId) {
        return productCombinationRepository.max(productId);
    }

    @Override
    public ProductCombinationResponse findByProductIdAndUniqueStringId(ProductCombinationRequest request) {
        String uniqueStringId = Utils.getUniqueStringId(request.getProductVariantName());
        ProductCombinations response = null;
        if(request.getProductVariantName()!= null){
            response = productCombinationRepository
                    .findByProductIdAndUniqueStringId(request.getProductId(),
                            uniqueStringId);
        } else {
                 Products product = productRepository.findByIdAndIsActiveTrue(request.getProductId());
            response = ProductCombinations.builder()
                    .price(product.getOriginalPrice())
                    .quantity(product.getOriginalQuantity())
                    .build();
        }
        return response != null ? ProductCombinationResponse.builder()
                .price(response.getQuantity()==0 ?  "Hết hàng" : "₫"+ Utils.formatNumber(response.getPrice()))
                .quantity(response.getQuantity()==0 ? 0: response.getQuantity())
                .build():
                ProductCombinationResponse.builder()
                        .price("Hết hàng")
                        .quantity(0)
                        .build()
                ;
    }

    @Override
    public boolean deleteByProductId(Products product) {
        List<ProductCombinations> entity = productCombinationRepository.findByProductAndIsActiveTrue(product);
        if (entity == null) return true;
        entity.forEach((e) -> {
            e.setActive(false);
            productCombinationRepository.save(e);
        });
        return true;
    }


}
