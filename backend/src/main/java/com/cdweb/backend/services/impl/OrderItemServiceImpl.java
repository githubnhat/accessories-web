package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.OrderItemConverter;
import com.cdweb.backend.converters.ProductConverter;
import com.cdweb.backend.entities.*;
import com.cdweb.backend.payloads.requests.OrderItemRequest;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.payloads.responses.OrderItemResponse;
import com.cdweb.backend.payloads.responses.ProductCombinationResponse;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.repositories.OrderItemRepository;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.repositories.ThumbnailRepository;
import com.cdweb.backend.services.ICartItemService;
import com.cdweb.backend.services.IOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements IOrderItemService {

    private final ProductRepository productRepository;

    private final ProductServiceImpl productService;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemConverter orderItemConverter;
    private final ThumbnailRepository thumbnailRepository;


    private final ICartItemService cartItemService;
    @Override
    public List<OrderItemResponse> findByOrderAndOrderByModifiedDateDesc(Orders order) {
        return orderItemRepository.findByOrderOrderByModifiedDateDesc(order)
                .stream()
                .map(orderItemConverter :: toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemResponse> saveOrderItemList(Orders order, List<OrderItemRequest> requests) {
        List<OrderItemResponse> responses = new ArrayList<>();

            requests.forEach(item ->{
                OrderItems newEntity = orderItemConverter.toEntity(item);
                Products product = productRepository.findByIdAndIsActiveTrue(item.getProductId());
                newEntity.setDescription(product.getDescription());
                newEntity.setProductName(product.getProductName());
                newEntity.setCategories(product.getCategories());
                newEntity.setBrands(product.getBrands());

                List<Thumbnails> thumbnails = thumbnailRepository.findByProductAndIsActiveTrue(product);
                newEntity.setImageLink((thumbnails.size() == 0 ? null : thumbnails.get(0).getImageLink()));

                newEntity.setOrder(order);

                orderItemRepository.save(newEntity);
                Long[] ids = {item.getCartId()};
                cartItemService.delete(ids, order.getUser());

                responses.add(orderItemConverter.toResponse(newEntity));
            });

        return responses;
    }
}
