package com.eric.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eric.ecommerce.model.AppUser;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.model.Role;
import com.eric.ecommerce.repository.CartItemRepository;
import com.eric.ecommerce.repository.CartRepository;
import com.eric.ecommerce.repository.ProductRepository;
import com.eric.ecommerce.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

	@Mock
	private CartRepository cartRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CartItemRepository cartItemRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private CartService cartService;

	@Test
	void deveRetornarCarrinhoExistente() {

		String email = "usuario@email.com";

		AppUser user = new AppUser(1, "Eric", email, "123456", Role.USER);

		Cart cart = new Cart();
		cart.setId(1);
		cart.setUser(user);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		when(cartRepository.findByUser_Email(email)).thenReturn(Optional.of(cart));

		Cart resultado = cartService.getOrCreateCart(email);

		assertEquals(1, resultado.getId());

		verify(userRepository).findByEmail(email);

	}

	@Test
	void deveCriarCarrinhoQuandoNaoExistir() {

		String email = "usuario@email.com";

		AppUser user = new AppUser(1, "Eric", email, "123456", Role.USER);

		Cart cartSalvo = new Cart();
		cartSalvo.setId(1);
		cartSalvo.setUser(user);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		when(cartRepository.findByUser_Email(email)).thenReturn(Optional.empty());

		when(cartRepository.save(any(Cart.class))).thenReturn(cartSalvo);

		Cart resultado = cartService.getOrCreateCart(email);

		assertEquals(1, resultado.getId());
		assertEquals(email, resultado.getUser().getEmail());

		verify(userRepository).findByEmail(email);
		verify(cartRepository).findByUser_Email(email);
		verify(cartRepository).save(any(Cart.class));

	}

}
