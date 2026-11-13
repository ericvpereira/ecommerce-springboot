package com.eric.ecommerce.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.dto.AddCartItemRequest;
import com.eric.ecommerce.dto.CartItemResponse;
import com.eric.ecommerce.dto.CartResponse;
import com.eric.ecommerce.dto.UpdateCartItemRequest;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.security.JwtAuthenticationFilter;
import com.eric.ecommerce.service.CartService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CartService cartService;

	@MockitoBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void deveBuscarCarrinhoComSucesso() throws Exception {

		String email = "usuario@email.com";

		Cart cart = new Cart();
		cart.setId(1);

		CartResponse response = new CartResponse();
		response.setId(1);
		response.setItems(List.of());
		response.setTotal(BigDecimal.ZERO);

		when(cartService.getOrCreateCart(email)).thenReturn(cart);
		when(cartService.toResponse(cart)).thenReturn(response);

		TestingAuthenticationToken authentication = new TestingAuthenticationToken(email, null);

		mockMvc.perform(get("/api/cart").principal(authentication)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.items").isArray())
				.andExpect(jsonPath("$.items").isEmpty()).andExpect(jsonPath("$.total").value(0));

		verify(cartService).getOrCreateCart(email);
		verify(cartService).toResponse(cart);

	}

	@Test
	void deveAdicionarItemAoCarrinhoComSucesso() throws Exception {

		String email = "usuario@email.com";

		AddCartItemRequest request = new AddCartItemRequest();
		request.setProductId(1);
		request.setQuantidade(2);

		Cart cart = new Cart();
		cart.setId(1);

		CartItemResponse itemResponse = new CartItemResponse();

		itemResponse.setProductId(1);
		itemResponse.setNome("Notebook");
		itemResponse.setPreco(new BigDecimal("3500.00"));
		itemResponse.setQuantidade(2);
		itemResponse.setSubtotal(new BigDecimal("7000.00"));

		CartResponse response = new CartResponse();
		response.setId(1);
		response.setItems(List.of(itemResponse));
		response.setTotal(new BigDecimal("7000.00"));

		when(cartService.addProduct(email, 1, 2)).thenReturn(cart);
		when(cartService.toResponse(cart)).thenReturn(response);

		TestingAuthenticationToken authentication = new TestingAuthenticationToken(email, null);

		mockMvc.perform(post("/api/cart/items").principal(authentication).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.items").isArray())
				.andExpect(jsonPath("$.items[0].productId").value(1))
				.andExpect(jsonPath("$.items[0].nome").value("Notebook"))
				.andExpect(jsonPath("$.items[0].preco").value(3500.00))
				.andExpect(jsonPath("$.items[0].quantidade").value(2))
				.andExpect(jsonPath("$.items[0].subtotal").value(7000.00))
				.andExpect(jsonPath("$.total").value(7000.00));

		verify(cartService).addProduct(email, 1, 2);
		verify(cartService).toResponse(cart);
	}

	@Test
	void deveRetornarBadRequestQuandoQuantidadeForInvalida() throws Exception {

		String email = "usuario@email.com";

		AddCartItemRequest request = new AddCartItemRequest();
		request.setProductId(1);
		request.setQuantidade(0);

		TestingAuthenticationToken authentication = new TestingAuthenticationToken(email, null);

		mockMvc.perform(post("/api/cart/items").principal(authentication).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Erro de validação"))
				.andExpect(jsonPath("$.errors.quantidade").exists());

		verifyNoInteractions(cartService);

	}

	@Test
	void deveAtualizarQuantidadeDoItemComSucesso() throws Exception {

		String email = "usuario@email.com";
		Integer productId = 1;

		UpdateCartItemRequest request = new UpdateCartItemRequest();
		request.setQuantidade(5);

		Cart cart = new Cart();
		cart.setId(1);

		CartItemResponse itemResponse = new CartItemResponse();
		itemResponse.setProductId(1);
		itemResponse.setNome("Notebook");
		itemResponse.setPreco(new BigDecimal("3500.00"));
		itemResponse.setQuantidade(5);
		itemResponse.setSubtotal(new BigDecimal("17500.00"));

		CartResponse response = new CartResponse();
		response.setId(1);
		response.setItems(List.of(itemResponse));
		response.setTotal(new BigDecimal("17500.00"));

		when(cartService.updateQuantity(email, productId, 5)).thenReturn(cart);
		when(cartService.toResponse(cart)).thenReturn(response);

		TestingAuthenticationToken authentication = new TestingAuthenticationToken(email, null);

		mockMvc.perform(put("/api/cart/items/{productId}", productId).principal(authentication)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.items[0].productId").value(1))
				.andExpect(jsonPath("$.items[0].quantidade").value(5))
				.andExpect(jsonPath("$.items[0].subtotal").value(17500.00))
				.andExpect(jsonPath("$.total").value(17500.00));

		verify(cartService).updateQuantity(email, productId, 5);
		verify(cartService).toResponse(cart);

	}

	@Test
	void deveRetornarBadRequestAoAtualizarComQuantidadeInvalida() throws Exception {

		String email = "usuario@email.com";
		Integer productId = 1;

		UpdateCartItemRequest request = new UpdateCartItemRequest();
		request.setQuantidade(0);

		TestingAuthenticationToken authentication = new TestingAuthenticationToken(email, null);

		mockMvc.perform(put("/api/cart/items/{productId}", productId).principal(authentication)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Erro de validação"))
				.andExpect(jsonPath("$.errors.quantidade").exists());

		verifyNoInteractions(cartService);

	}

	@Test
	void deveRemoverItemDoCarrinhoComSucesso() throws Exception {

		String email = "usuario@email.com";
		Integer productId = 1;

		Cart cart = new Cart();
		cart.setId(1);

		CartResponse response = new CartResponse();
		response.setId(1);
		response.setItems(List.of());
		response.setTotal(BigDecimal.ZERO);

		when(cartService.removeProduct(email, productId)).thenReturn(cart);
		when(cartService.toResponse(cart)).thenReturn(response);

		TestingAuthenticationToken authentication = new TestingAuthenticationToken(email, null);

		mockMvc.perform(delete("/api/cart/items/{productId}", productId).principal(authentication))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.items").isArray()).andExpect(jsonPath("$.items").isEmpty())
				.andExpect(jsonPath("$.total").value(0));

		verify(cartService).removeProduct(email, productId);
		verify(cartService).toResponse(cart);

	}

}
