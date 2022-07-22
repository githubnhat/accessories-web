package com.cdweb.backend.services.impl;

import com.cdweb.backend.common.Utils;
import com.cdweb.backend.converters.CartItemConverter;
import com.cdweb.backend.entities.*;
import com.cdweb.backend.payloads.requests.CartItemRequest;
import com.cdweb.backend.payloads.responses.CartItemResponse;
import com.cdweb.backend.repositories.CartItemRepository;
import com.cdweb.backend.repositories.ProductCombinationRepository;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.repositories.ThumbnailRepository;
import com.cdweb.backend.services.ICartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;

    private final ThumbnailRepository thumbnailRepository;

    private final ProductCombinationRepository productCombinationRepository;

    private final ProductRepository productRepository;

    private final CartItemConverter cartItemConverter;

    @Override
    public CartItemResponse addToCart(CartItemRequest request, Users user) {
        Products product = productRepository.findByIdAndIsActiveTrue(request.getProductId());
        ProductCombinations productCombination = null;
        if (request.getProductVariantName() != null) {
            productCombination = productCombinationRepository
                    .findByProductIdAndUniqueStringId(request.getProductId(),
                            Utils.getUniqueStringId(request.getProductVariantName()));
        } else {
            productCombination = productCombinationRepository.findByProductId(request.getProductId());
        }
        CartItems cart = cartItemRepository.findByProductAndProductCombinationAndUser(product, productCombination,
                user);
        CartItems entity = null;

        if (cart != null) {
            int updateQuantity = request.getQuantity() + cart.getQuantity();
            cart.setQuantity(updateQuantity);
            entity = cartItemRepository.save(cart);
        } else {
            entity = cartItemRepository.save(
                    CartItems.builder()
                            .user(user)
                            .product(product)
                            .productCombination(productCombination)
                            .quantity(request.getQuantity())
                            .build());
        }
        List<Thumbnails> thumbnails = thumbnailRepository.findByProductAndIsActiveTrue(product);
        CartItemResponse response = cartItemConverter.toResponse(entity);
        response.setThumbnail(thumbnails.size() == 0 ? null : thumbnails.get(0).getImageLink());
        return response;

    }

    @Override
    public List<CartItemResponse> findByUserOrderByCreatedDateDesc(Users user) {
        List<CartItemResponse> responses = new ArrayList<>();
        List<CartItems> entities = cartItemRepository.findByUserOrderByCreatedDateDesc(user);
        entities.forEach(e -> {
            List<Thumbnails> thumbnails = thumbnailRepository.findByProductAndIsActiveTrue(e.getProduct());
            CartItemResponse response = cartItemConverter.toResponse(e);
            response.setThumbnail(thumbnails.size() == 0 ? null : thumbnails.get(0).getImageLink());
            responses.add(response);
        });
        return responses;
    }

    @Override
    public boolean delete(Long[] ids, Users user) {
        boolean exists = true;
        for (Long id : ids) {
            if (!cartItemRepository.existsById(id))
                exists = false;
        }
        if (exists) {
            for (Long id : ids) {
                cartItemRepository.deleteById(id);
            }
        }
        return exists;
    }

    @Override
    public CartItemResponse updateCart(CartItemRequest request) {
        CartItems cart = cartItemRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalStateException("Not found cart!"));
        if (cart != null) {
            cart.setQuantity(request.getQuantity());
            CartItems updatedCart = cartItemRepository.save(cart);
            return cartItemConverter.toResponse(updatedCart);
        } else {
            return null;
        }

    }
}
