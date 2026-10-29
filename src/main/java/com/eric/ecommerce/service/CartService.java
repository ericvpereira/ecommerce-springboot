package com.eric.ecommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eric.ecommerce.dto.CartItemResponse;
import com.eric.ecommerce.dto.CartResponse;
import com.eric.ecommerce.exceptions.InvalidCartQuantityException;
import com.eric.ecommerce.exceptions.ProductNotFoundException;
import com.eric.ecommerce.exceptions.UserNotFoundException;
import com.eric.ecommerce.model.AppUser;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.model.CartItem;
import com.eric.ecommerce.model.Product;
import com.eric.ecommerce.repository.CartItemRepository;
import com.eric.ecommerce.repository.CartRepository;
import com.eric.ecommerce.repository.ProductRepository;
import com.eric.ecommerce.repository.UserRepository;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	public CartService(CartRepository cartRepository, UserRepository userRepository,
			CartItemRepository cartItemRepository, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
	}

	public Cart getOrCreateCart(String email) {

		AppUser user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("Usuário com email [" + email + "] não encontrado"));

		Optional<Cart> cartOptional = cartRepository.findByUser_Email(email);

		if (cartOptional.isPresent()) {

			return cartOptional.get();

		} else {
			Cart cart = new Cart();
			cart.setUser(user);

			return cartRepository.save(cart);
		}

	}

	public Cart addProduct(String email, Integer productId, Integer quantidade) {

		if (quantidade == null || quantidade < 1) {
			throw new InvalidCartQuantityException("A quantidade deve ser maior ou igual a 1");
		}

		Cart cart = getOrCreateCart(email);

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Produto com ID [" + productId + "] não encontrado"));

		Optional<CartItem> itemOptional = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId);

		if (itemOptional.isPresent()) {

			CartItem item = itemOptional.get();

			item.setQuantidade(item.getQuantidade() + quantidade);

			cartItemRepository.save(item);

		} else {

			CartItem item = new CartItem();

			item.setCart(cart);
			item.setProduct(product);
			item.setQuantidade(quantidade);

			cartItemRepository.save(item);

		}

		return cart;

	}

	public CartResponse toResponse(Cart cart) {

		List<CartItemResponse> itemsResponse = new ArrayList<>();

		BigDecimal total = BigDecimal.ZERO;

		for (CartItem item : cart.getItems()) {

			Product product = item.getProduct();

			BigDecimal subtotal = product.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));

			CartItemResponse response = new CartItemResponse();

			response.setProductId(product.getId());
			response.setNome(product.getNome());
			response.setPreco(product.getPreco());
			response.setQuantidade(item.getQuantidade());
			response.setSubtotal(subtotal);

			itemsResponse.add(response);

			total = total.add(subtotal);
		}

		CartResponse cartResponse = new CartResponse();

		cartResponse.setId(cart.getId());
		cartResponse.setItems(itemsResponse);
		cartResponse.setTotal(total);

		return cartResponse;

	}

}
