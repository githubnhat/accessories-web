package com.cdweb.backend.services;

import com.cdweb.backend.payloads.responses.OrderResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    int totalOrder();

    List<OrderResponse> getAllOrdersPaging(Pageable pageable);

    OrderResponse getOrderDetail(Long orderId);
}
