package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.ProductAttributeConverter;
import com.cdweb.backend.converters.ProductAttributeVariantConverter;
import com.cdweb.backend.converters.ProductConverter;
import com.cdweb.backend.entities.*;
import com.cdweb.backend.payloads.requests.AttributeAndVariantsRequest;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.*;
import com.cdweb.backend.repositories.*;
import com.cdweb.backend.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    private final ProductConverter productConverter;

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final IAttributeService attributeService;

    private final IVariantService variantService;

    private final IProductAttributeService productAttributeService;

    private final IProductGalleryService productGalleryService;

    private final IProductAttributeVariantService productAttributeVariantService;

    private final IProductCombinationService productCombinationService;

    private final ProductAttributeConverter productAttributeConverter;



    @Override
    public List<ProductResponse> findAll(Pageable pageable) {
        List<ProductResponse> response = new ArrayList<>();
        List<Products> entities = productRepository.findAll(pageable).getContent();
        List<Products> result = new ArrayList<>();
        for (Products p :
                entities) {
            if (p.isActive()) {
                result.add(p);
            }
        }
//        result.forEach(p -> response.add(productConverter.toResponse(p, productGalleryRepository.findByProduct(p))));
        return response;
    }

    @Override
    public int totalItem() {
        return (int) productRepository.count();
    }

    @Override
    public ProductResponse findById(Long id) {
//        Products foundEntity = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found product!"));
//        return (foundEntity != null) ? productConverter.toResponse(foundEntity, productGalleryRepository.findByProduct(foundEntity)) : null;
        return null;

    }

    @Override
    public ProductResponse findByProductName(String productName) {
        Products entity = productRepository.findByProductNameAndIsActiveTrue(productName);
//        return productConverter.toResponse(entity, productGalleryRepository.findByProduct(entity));
        return null;
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        Products entity = productRepository.findByProductNameAndIsActiveTrue(request.getProductName());
        if (entity == null) {
            Products newEntity = productConverter.toEntity(request);
            Categories category = categoryRepository.findByNameAndIsActiveTrue(request.getCategoryName());
            Brands brand = brandRepository.findByNameAndIsActiveTrue(request.getBrandName());
            newEntity.setCategories(category);
            newEntity.setBrands(brand);
            newEntity.setActive(true);

            //lưu san phẩm
            Products savedEntity = productRepository.save(newEntity);
            //Lưu hình
            List<ProductGalleryResponse> productGalleryResponses = productGalleryService.save(savedEntity, request.getImageLinks());

            List<String> listOfAttributeNames = new ArrayList<>();
            List<AttributeAndVariantsRequest> attributesAndVariantsRequest = new ArrayList<>();
            request.getAttributes().forEach(a -> {
                listOfAttributeNames.add(a.getAttributeName());
                attributesAndVariantsRequest.add(a);
            });
            //lưu attribute
            List<AttributeResponse> savedAttribute = attributeService.saveListAttribute(listOfAttributeNames);
            //Lưu variant theo attribute
            List<AttributeAndVariantsResponse> listVariantOfAttrRs = variantService.checkAndSaveListVariants(attributesAndVariantsRequest);

            List<Attributes> listAttributes = new ArrayList<>();
            savedAttribute.forEach(a -> {
                Attributes attr = Attributes.builder()
                        .attributeName(a.getAttributeName())
                        .build();
                attr.setId(a.getId());
                listAttributes.add(attr);
            });

            //lưu product_attribute
            List<ProductAttributes> productAttributes = productAttributeService.saveListProductAttribute(listAttributes, savedEntity);
            List<ProductAttributeResponse> productAttributeRs = productAttributes.stream()
                    .map(productAttributeConverter :: toResponse).collect(Collectors.toList());
            List<Variants> variants = new ArrayList<>();

            // tìm list variant theo tên và attributeName
            listVariantOfAttrRs.forEach(variantOfAttribute -> variantOfAttribute.getVariantNames().forEach(variantName -> {
                Attributes attributes = attributeService.findByAttributeNameAndIsActiveTrue(variantOfAttribute.getAttributeName());
                Variants variantEntity = variantService.findByVariantNameAndAttributeIdAndIsActiveTrue(variantName,attributes.getId() );
                variants.add(variantEntity);
            }));

            // so sanh attributeId của hai bảng product_attribute và bảng variant để lưu bảng product_attribute_variant
            List<ProductAttributeVariantResponse> proAttrVarRs = productAttributeVariantService.save(productAttributes, variants);

            //lưu product combination
            List<ProductCombinationResponse> proComRs = productCombinationService.saveListCombinations(savedEntity, request.getCombinations());
            return productConverter.toResponse(savedEntity, productGalleryResponses, listVariantOfAttrRs, proComRs);
        }
        return null;
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
