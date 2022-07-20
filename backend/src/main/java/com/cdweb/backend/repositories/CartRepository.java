package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Carts;
import com.cdweb.backend.entities.ProductCombinations;
import com.cdweb.backend.entities.Products;
import com.cdweb.backend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Carts, Long> {

    Carts findByProductAndProductCombinationAndUser(Products product, ProductCombinations productCombination, Users user);

    List<Carts> findByUserOrderByCreatedDateDesc(Users user);
}
