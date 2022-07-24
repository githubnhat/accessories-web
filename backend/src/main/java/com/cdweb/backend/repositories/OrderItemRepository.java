package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.OrderItems;
import com.cdweb.backend.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findByOrderOrderByModifiedDateDesc(Orders order);
}
