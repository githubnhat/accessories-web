package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.OrderConverter;
import com.cdweb.backend.payloads.responses.OrderResponse;
import com.cdweb.backend.repositories.OrderRepository;
import com.cdweb.backend.services.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;

    private final OrderConverter orderConverter;

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
}
