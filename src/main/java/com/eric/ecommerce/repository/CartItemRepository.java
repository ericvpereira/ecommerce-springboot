package com.eric.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eric.ecommerce.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	Optional<CartItem> findByCart_IdAndProduct_Id(Integer cartId, Integer productId);

}
