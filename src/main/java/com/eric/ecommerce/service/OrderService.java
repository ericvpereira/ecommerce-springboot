package com.eric.ecommerce.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.eric.ecommerce.exceptions.EmptyCartException;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.model.CartItem;
import com.eric.ecommerce.model.Order;
import com.eric.ecommerce.model.OrderItem;
import com.eric.ecommerce.model.OrderStatus;
import com.eric.ecommerce.repository.OrderItemRepository;
import com.eric.ecommerce.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final CartService cartService;

	public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			CartService cartService) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.cartService = cartService;
	}

	@Transactional
	public Order checkout(String email) {

		Cart cart = cartService.getOrCreateCart(email);

		if (cart.getItems().isEmpty()) {

			throw new EmptyCartException("Não é possível finalizar um carrinho vazio");

		}

		Order order = new Order();

		order.setUser(cart.getUser());

		order.setDataCriacao(LocalDateTime.now());

		order.setStatus(OrderStatus.CREATED);

		BigDecimal total = BigDecimal.ZERO;

		for (CartItem item : cart.getItems()) {

			BigDecimal subtotal = item.getProduct().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));

			total = total.add(subtotal);

		}

		order.setTotal(total);

		Order savedOrder = orderRepository.save(order);

		for (CartItem item : cart.getItems()) {

			OrderItem orderItem = new OrderItem();

			orderItem.setOrder(savedOrder);
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantidade(item.getQuantidade());
			orderItem.setPrecoUnitario(item.getProduct().getPreco());

			orderItemRepository.save(orderItem);
			savedOrder.getItems().add(orderItem);

		}

		cartService.clearCart(email);

		return savedOrder;

	}

};