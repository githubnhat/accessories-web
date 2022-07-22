package com.cdweb.backend.services;

import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.CartItemRequest;
import com.cdweb.backend.payloads.responses.CartItemResponse;

import java.util.List;

public interface ICartItemService {

    CartItemResponse addToCart(CartItemRequest request, Users user);

    List<CartItemResponse> findByUserOrderByCreatedDateDesc(Users user);

    boolean delete(Long[] ids, Users user);

    CartItemResponse updateCart(CartItemRequest request);
}
