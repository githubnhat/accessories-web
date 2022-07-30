package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Orders;
import com.cdweb.backend.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Page findByUserOrderByModifiedDateDesc(Users user, Pageable pageable);

    Long countByUser(Users user);

    Optional<Orders> findById(Long orderId);
}
