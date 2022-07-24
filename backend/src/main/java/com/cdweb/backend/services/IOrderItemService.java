package com.cdweb.backend.services;

import com.cdweb.backend.entities.Orders;
import com.cdweb.backend.payloads.requests.OrderItemRequest;
import com.cdweb.backend.payloads.responses.OrderItemResponse;

import java.util.List;

public interface IOrderItemService {
    List<OrderItemResponse> findByOrderAndOrderByModifiedDateDesc(Orders order);

    List<OrderItemResponse> saveOrderItemList(Orders order, List<OrderItemRequest> requests);
}
