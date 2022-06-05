package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.ProductConverter;
import com.cdweb.backend.entities.Brands;
import com.cdweb.backend.entities.Categories;
import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.repositories.BrandRepository;
import com.cdweb.backend.repositories.CategoryRepository;
import com.cdweb.backend.repositories.ProductGalleryRepository;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    private final ProductConverter productConverter;

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final ProductGalleryRepository productGalleryRepository;

    @Override
    public List<ProductResponse> findAll(Pageable pageable) {
        List<ProductResponse> response = new ArrayList<>();
        List<Products> entities = productRepository.findAll(pageable).getContent();
        List<Products> result = new ArrayList<>();
        for (Products p :
                entities) {
            if (p.isActive()){
                result.add(p);
            }
        }
        result.forEach(p -> response.add(productConverter.toResponse(p, productGalleryRepository.findByProducts(p))));
        return response;
    }

    @Override
    public int totalItem() {
        return (int) productRepository.count();
    }

    @Override
    public ProductResponse findById(Long id) {
        Products foundEntity = productRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Not found product!"));
        return (foundEntity!= null) ? productConverter.toResponse(foundEntity, productGalleryRepository.findByProducts(foundEntity)) : null;

    }

    @Override
    public List<ProductResponse> findByProductName(String productName) {
        List<ProductResponse> response = new ArrayList<>();
        List<Products> entities = productRepository.findByProductNameAndIsActiveTrue(productName);
        entities.forEach(p -> response.add(productConverter.toResponse(p, productGalleryRepository.findByProducts(p))));
        return response;
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        Products entity = productConverter.toEntity(request);
        entity.setId(request.getId());
        if (entity.getId() != null) {
           Products oldProductEntity = productRepository.findById(entity.getId()).orElseThrow(()-> new IllegalArgumentException("Not found product!"));
           Categories category = categoryRepository.findByName(request.getCategoryName());
           Brands branch = brandRepository.findByName(request.getBranchName());
            List<ProductGalleries> productGalleries = new ArrayList<>();
            List<ProductGalleries> oldProductGalleries = productGalleryRepository.findByProducts(oldProductEntity);

           entity = oldProductEntity
                    .builder()
                    .productName(entity.getProductName())
                    .year(entity.getYear())
                    .price(entity.getPrice())
                    .categories(category)
                    .brands(branch)
                    .build();
            entity.setId(request.getId());// chu ý cần sửa
        } else {
            entity = productConverter.toEntity(request);
            entity.setActive(true);
        }
        Products insertedProduct = productRepository.save(entity);
        List<ProductGalleries> productGalleries = new ArrayList<>();
        request.getImageLinks().forEach(e -> productGalleries
                .add(productGalleryRepository
                        .save(ProductGalleries.builder()
                                .imageLink(e)
                                .products(insertedProduct)
                                .build())));

        return productConverter.toResponse(insertedProduct, productGalleries);
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
               Products entity = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found product"));
               entity.setActive(false);
               productRepository.save(entity);
            }
        }
        return exists;
    }

    @Override
    public List<ProductResponse> getArrivalProducts() {
        return null;
    }
}
