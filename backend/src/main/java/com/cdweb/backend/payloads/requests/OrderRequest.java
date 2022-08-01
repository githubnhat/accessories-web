package com.cdweb.backend.payloads.requests;

import com.cdweb.backend.entities.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long id;
    private String address;
    private String phone;
    private String totalBill;
    private String status;
    private List<OrderItemRequest> orderItems;
}
