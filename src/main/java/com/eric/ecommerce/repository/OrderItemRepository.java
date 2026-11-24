package com.eric.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.ecommerce.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
