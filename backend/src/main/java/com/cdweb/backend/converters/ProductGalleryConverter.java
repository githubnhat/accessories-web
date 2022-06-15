package com.cdweb.backend.converters;

import com.cdweb.backend.entities.ProductGalleries;
import com.cdweb.backend.payloads.responses.ProductGalleryResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductGalleryConverter {
    public ProductGalleryResponse toResponse(ProductGalleries entity){
        return ProductGalleryResponse
                .builder()
                    .id(entity.getId())
                    .productId(entity.getProduct().getId())
                    .imageLink(entity.getImageLink())
                .build();
    }
}
