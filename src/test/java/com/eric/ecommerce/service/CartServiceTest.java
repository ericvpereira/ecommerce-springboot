package com.eric.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eric.ecommerce.exceptions.InvalidCartQuantityException;
import com.eric.ecommerce.exceptions.UserNotFoundException;
import com.eric.ecommerce.model.AppUser;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.model.CartItem;
import com.eric.ecommerce.model.Product;
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

	@Test
	void deveLancarExcecaoQuandoUsuarioNaoExistir() {

		String email = "naoexiste@email.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> cartService.getOrCreateCart(email));

		verify(userRepository).findByEmail(email);
		verify(cartRepository, never()).findByUser_Email(email);

	}

	@Test
	void deveAdicionarProdutoNovoAoCarrinho() {

		String email = "usuario@email.com";

		AppUser user = new AppUser(1, "Eric", email, "123456", Role.USER);

		Cart cart = new Cart();
		cart.setId(1);
		cart.setUser(user);

		Product product = new Product(1, null, "Notebook", new BigDecimal("3500.00"));

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		when(cartRepository.findByUser_Email(email)).thenReturn(Optional.of(cart));

		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		when(cartItemRepository.findByCart_IdAndProduct_Id(1, 1)).thenReturn(Optional.empty());

		Cart resultado = cartService.addProduct(email, 1, 2);

		assertEquals(1, resultado.getItems().size());

		assertEquals(2, resultado.getItems().get(0).getQuantidade());

		assertEquals(1, resultado.getItems().get(0).getProduct().getId());

		verify(cartItemRepository).save(any(CartItem.class));

	}

	@Test
	void deveSomarQuantidadeQuandoProdutoJaExistirNoCarrinho() {

		String email = "usuario@email.com";

		AppUser user = new AppUser(1, "Eric", email, "123456", Role.USER);

		Cart cart = new Cart();
		cart.setId(1);
		cart.setUser(user);

		Product product = new Product(1, null, "Notebook", new BigDecimal("3500.00"));

		CartItem itemExistente = new CartItem(1, cart, product, 2);

		cart.getItems().add(itemExistente);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		when(cartRepository.findByUser_Email(email)).thenReturn(Optional.of(cart));

		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		when(cartItemRepository.findByCart_IdAndProduct_Id(1, 1)).thenReturn(Optional.of(itemExistente));

		Cart resultado = cartService.addProduct(email, 1, 3);

		assertEquals(5, itemExistente.getQuantidade());
		assertEquals(1, resultado.getItems().size());

		verify(cartItemRepository).save(itemExistente);

	}

	@Test
	void deveLancarExcecaoQuandoQuantidadeForInvalida() {

		String email = "usuario@email.com";
		Integer quantidade = 0;

		assertThrows(InvalidCartQuantityException.class, () -> cartService.addProduct(email, 1, quantidade));

		verify(userRepository, never()).findByEmail(email);
		verify(productRepository, never()).findById(1);

	}

	@Test
	void deveLancarExcecaoAoAtualizarComQuantidadeInvalida() {

		String email = "usuario@email.com";
		Integer productId = 1;
		Integer quantidade = 0;

		assertThrows(InvalidCartQuantityException.class,
				() -> cartService.updateQuantity(email, productId, quantidade));

		verify(userRepository, never()).findByEmail(email);
		verify(cartRepository, never()).findByUser_Email(email);
		verify(cartItemRepository, never()).findByCart_IdAndProduct_Id(any(), any());

	}

}
