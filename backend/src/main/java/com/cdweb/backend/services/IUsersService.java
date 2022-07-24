package com.cdweb.backend.services;

import com.cdweb.backend.entities.Orders;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.responses.AddressResponse;
import com.cdweb.backend.payloads.responses.OrderResponse;
import com.cdweb.backend.payloads.responses.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsersService {
    List<UserResponse> getAllUsers();

    AddressResponse findMainAddress(Users user);

    List<AddressResponse> getAllAddresses(Users user);

    AddressResponse save(AddressRequest address, Users user);

    Boolean deleteAddress(Long id, Users user);

    List<OrderResponse> getOrders(Pageable pageable, Users user);

    OrderResponse save(OrderRequest orderRequest, Users user);

    Boolean updateOrderStatus(Long orderId, String status);

    int totalOrder();

    int totalOrderByUser(Users user);
}
