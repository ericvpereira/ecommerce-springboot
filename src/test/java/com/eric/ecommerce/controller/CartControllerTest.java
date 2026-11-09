package com.eric.ecommerce.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.dto.CartResponse;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.security.JwtAuthenticationFilter;
import com.eric.ecommerce.service.CartService;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CartService cartService;

	@MockitoBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

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

}
