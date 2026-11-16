package com.eric.ecommerce.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.config.SecurityConfig;
import com.eric.ecommerce.controller.CartController;
import com.eric.ecommerce.controller.CategoriaController;
import com.eric.ecommerce.controller.ProductController;
import com.eric.ecommerce.dto.CartResponse;
import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.model.Cart;
import com.eric.ecommerce.service.CartService;
import com.eric.ecommerce.service.CategoriaService;
import com.eric.ecommerce.service.CustomUserDetailsService;
import com.eric.ecommerce.service.ProductService;

@WebMvcTest(controllers = { ProductController.class, CategoriaController.class, CartController.class })
@Import({ SecurityConfig.class, JwtAuthenticationFilter.class })
public class SecurityAuthorizationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@MockitoBean
	private CategoriaService categoriaService;

	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private CartService cartService;

	@MockitoBean
	private CustomUserDetailsService userDetailsService;

	@Test
	void deveRetornar401QuandoNaoEstiverAutenticado() throws Exception {

		mockMvc.perform(get("/api/products")).andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser(username = "user@email.com", roles = "USER")
	void devePermitirGetParaUsuarioUser() throws Exception {

		mockMvc.perform(get("/api/products")).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "user@email.com", roles = "USER")
	void deveNegarPostParaUsuarioUser() throws Exception {

		mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).content("""
				{
					"nome": "Informatica"
				}
				""")).andExpect(status().isForbidden());
		verifyNoInteractions(categoriaService);

	}

	@Test
	@WithMockUser(username = "admin@email.com", roles = "ADMIN")
	void devePermitirPostParaUsuarioAdmin() throws Exception {

		CategoriaDTO categoriaCriada = new CategoriaDTO(1, "Informatica");

		when(categoriaService.save(any(CategoriaDTO.class))).thenReturn(categoriaCriada);

		mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).content("""
				{
					"nome": "Informatica"
				}
				""")).andExpect(status().isCreated());
	}

	@Test
	void deveRetornar401AoAcessarCarrinhoSemAutenticacao() throws Exception {

		mockMvc.perform(get("/api/cart")).andExpect(status().isUnauthorized());

		verifyNoInteractions(cartService);

	}

	@Test
	@WithMockUser(username = "user@email.com", roles = "USER")
	void devePermitirAcessoAoCarrinhoParaUsuarioAutenticado() throws Exception {

		String email = "user@email.com";

		Cart cart = new Cart();
		cart.setId(1);

		CartResponse response = new CartResponse();
		response.setId(1);
		response.setItems(List.of());
		response.setTotal(BigDecimal.ZERO);

		when(cartService.getOrCreateCart(email)).thenReturn(cart);
		when(cartService.toResponse(cart)).thenReturn(response);

		mockMvc.perform(get("/api/cart")).andExpect(status().isOk());

		verify(cartService).getOrCreateCart(email);
		verify(cartService).toResponse(cart);

	}
}
