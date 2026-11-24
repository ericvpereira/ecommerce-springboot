package com.eric.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.ecommerce.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByUser_Email(String email);

}