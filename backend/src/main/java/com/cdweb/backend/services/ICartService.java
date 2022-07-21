package com.cdweb.backend.services;

import com.cdweb.backend.entities.Carts;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.CartRequest;
import com.cdweb.backend.payloads.responses.CartResponse;

import java.util.List;

public interface ICartService {

    CartResponse addToCart(CartRequest request, Users user);

    List<CartResponse> findByUserOrderByCreatedDateDesc(Users user);

    boolean delete(Long[] ids, Users user);

    CartResponse updateCart(CartRequest request);
}
