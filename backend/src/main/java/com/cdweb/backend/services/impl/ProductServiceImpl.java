package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.ProductConverter;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    private final ProductConverter productConverter;

    @Override
    public List<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).getContent()
                .stream().map(productConverter::toResponse).collect(Collectors.toList());
    }

    @Override
    public int totalItem() {
        return (int) productRepository.count();
    }

    @Override
    public ProductResponse findById(Long id) {
        Optional<Products> foundEntity = productRepository.findById(id);
        return (foundEntity.isPresent()) ? productConverter.toResponse(foundEntity.get()) : null;

    }

    @Override
    public List<ProductResponse> findByProductName(String productName) {
        return productRepository.findByProductName(productName)
                .stream().map(productConverter::toResponse).collect(Collectors.toList());
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        Products entity = productConverter.toEntity(request);
        entity.setId(request.getId());
        if (entity.getId() != null) {
            Optional<Products> oldProductEntity = productRepository.findById(entity.getId());
            entity = oldProductEntity.get()
                    .builder()
                    .productName(entity.getProductName())
                    .year(entity.getYear())
                    .url(entity.getUrl())
                    .price(entity.getPrice())
                    .build();
            entity.setId(request.getId());// chu ý cần sửa
        } else {
            entity = productConverter.toEntity(request);
        }
        return productConverter.toResponse(productRepository.save(entity));
    }


    @Override
    public boolean delete(Long[] ids) {
        boolean exists = true;
        for (Long id : ids) {
            if (!productRepository.existsById(id)) exists = false;
        }
        if (exists) {
            for (Long id :
                    ids) {
                productRepository.deleteById(id);
            }
        }
        return exists;
    }
}
