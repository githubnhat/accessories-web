package com.cdweb.backend.services.impl;

import com.cdweb.backend.common.Utils;
import com.cdweb.backend.converters.CartConverter;
import com.cdweb.backend.entities.Carts;
import com.cdweb.backend.entities.ProductCombinations;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.CartRequest;
import com.cdweb.backend.payloads.responses.CartResponse;
import com.cdweb.backend.repositories.CartRepository;
import com.cdweb.backend.repositories.ProductCombinationRepository;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;

    private final ProductCombinationRepository productCombinationRepository;

    private final ProductRepository productRepository;

    private final CartConverter cartConverter;


    @Override
    public CartResponse addToCart(CartRequest request, Users user) {
        Products product = productRepository.findByIdAndIsActiveTrue(request.getProductId());
        ProductCombinations productCombination = productCombinationRepository
                .findByProductIdAndUniqueStringId(request.getProductId(),
                        Utils.getUniqueStringId(request.getProductVariantName()));
        Carts cart = cartRepository.findByProductAndProductCombinationAndUser(product, productCombination, user);
        Carts entity = null;
        if (cart != null) {
            int updateQuantity = request.getQuantity() + cart.getQuantity();
            cart.setQuantity(updateQuantity);
            entity = cartRepository.save(cart);

        } else {
            entity = cartRepository.save(
                    Carts.builder()
                            .user(user)
                            .product(product)
                            .productCombination(productCombination)
                            .quantity(request.getQuantity())
                            .build());
        }
        return cartConverter.toResponse(entity);


    }

    @Override
    public List<CartResponse> findByUserOrderByCreatedDateDesc(Users user) {
        List<CartResponse> responses = new ArrayList<>();
        List<Carts> entities = cartRepository.findByUserOrderByCreatedDateDesc(user);
        entities.forEach(e -> {
           responses.add(cartConverter.toResponse(e));
        });
        return responses;
    }
}
