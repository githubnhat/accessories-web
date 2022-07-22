package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.CartItems;
import com.cdweb.backend.entities.ProductCombinations;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    CartItems findByProductAndProductCombinationAndUser(Products product, ProductCombinations productCombination, Users user);

    List<CartItems> findByUserOrderByCreatedDateDesc(Users user);
}
