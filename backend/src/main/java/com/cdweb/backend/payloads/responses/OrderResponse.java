package com.cdweb.backend.payloads.responses;

import com.cdweb.backend.payloads.requests.OrderItemRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private String customerName;
    private String address;
    private String phone;
    private String status;
    private Date insertDate;
    private String totalBill;
    private List<OrderItemResponse> orderItems;
}
