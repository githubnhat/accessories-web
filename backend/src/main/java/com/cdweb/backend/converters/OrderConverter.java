package com.cdweb.backend.converters;


import com.cdweb.backend.entities.Orders;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.responses.OrderItemResponse;
import com.cdweb.backend.payloads.responses.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class OrderConverter {
    public Orders toEntity(OrderRequest request){
        return Orders.builder()
                .address(request.getAddress())
                .phone(request.getPhone())
                .totalBill(request.getTotalBill())
                .build();
    }

    public OrderResponse toResponse(Orders entity, List<OrderItemResponse> orderItems){
        return OrderResponse.builder()
                .id(entity.getId())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .totalBill(entity.getTotalBill())
                .status(entity.getStatus())
                .insertDate(entity.getCreatedDate())
                .orderItems(orderItems)
                .build();
    }
}
