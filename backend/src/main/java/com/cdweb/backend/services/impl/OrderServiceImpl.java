package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.OrderConverter;
import com.cdweb.backend.converters.OrderItemConverter;
import com.cdweb.backend.entities.OrderItems;
import com.cdweb.backend.entities.Orders;
import com.cdweb.backend.payloads.responses.OrderItemResponse;
import com.cdweb.backend.payloads.responses.OrderResponse;
import com.cdweb.backend.repositories.OrderItemRepository;
import com.cdweb.backend.repositories.OrderRepository;
import com.cdweb.backend.services.IOrderService;
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
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;

    private final OrderConverter orderConverter;

    private final OrderItemRepository orderItemRepository;

    private final OrderItemConverter orderItemConverter;

    @Override
    public int totalOrder() {
        return (int) orderRepository.count();
    }

    @Override
    public List<OrderResponse> getAllOrdersPaging(Pageable pageable) {
        List<OrderResponse> responses = orderRepository.findByOrderByModifiedDateDesc(pageable).getContent()
                .stream().map(orderConverter :: toResponse)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public OrderResponse getOrderDetail(Long orderId) {
        Orders orders = orderRepository.findById(orderId).get();
        if (orders != null) {
            List<OrderItems> orderItemsList = orderItemRepository.findByOrder_Id(orderId);
            List<OrderItemResponse> orderItemsResponse = new ArrayList<>();
            orderItemsList.forEach(e -> {
                orderItemsResponse.add(orderItemConverter.toResponse(e));
            });
            OrderResponse response = orderConverter.toResponse(orders, orderItemsResponse);
            return response;
        }
        return null;
    }
}
