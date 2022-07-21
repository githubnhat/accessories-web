package com.cdweb.backend.services.impl;

import com.cdweb.backend.common.Utils;
import com.cdweb.backend.converters.CartConverter;
import com.cdweb.backend.entities.*;
import com.cdweb.backend.payloads.requests.CartRequest;
import com.cdweb.backend.payloads.responses.CartResponse;
import com.cdweb.backend.repositories.CartRepository;
import com.cdweb.backend.repositories.ProductCombinationRepository;
import com.cdweb.backend.repositories.ProductRepository;
import com.cdweb.backend.repositories.ThumbnailRepository;
import com.cdweb.backend.services.ICartService;
import com.cdweb.backend.services.IThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;

    private final ThumbnailRepository thumbnailRepository;

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
        List<Thumbnails> thumbnails = thumbnailRepository.findByProductAndIsActiveTrue(product);
        CartResponse response = cartConverter.toResponse(entity);
        response.setThumbnail(thumbnails.get(0).getImageLink());
        return response;


    }

    @Override
    public List<CartResponse> findByUserOrderByCreatedDateDesc(Users user) {
        List<CartResponse> responses = new ArrayList<>();
        List<Carts> entities = cartRepository.findByUserOrderByCreatedDateDesc(user);
        entities.forEach(e -> {
            List<Thumbnails> thumbnails = thumbnailRepository.findByProductAndIsActiveTrue(e.getProduct());
            CartResponse response = cartConverter.toResponse(e);
            response.setThumbnail(thumbnails.get(0).getImageLink());
            responses.add(response);
        });
        return responses;
    }

    @Override
    public boolean delete(Long[] ids, Users user) {
        boolean exists = true;
        for (Long id : ids) {
            if (!cartRepository.existsById(id)) exists = false;
        }
        if (exists) {
            for (Long id :
                    ids) {
                cartRepository.deleteById(id);
            }
        }
        return exists;
    }

    @Override
    public CartResponse updateCart(CartRequest request) {
        Carts cart = cartRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalStateException("Not found cart!"));
        if(cart!= null){
            cart.setQuantity(request.getQuantity());
            Carts updatedCart = cartRepository.save(cart);
            return cartConverter.toResponse(updatedCart);
        } else {
            return null;
        }

    }
}
