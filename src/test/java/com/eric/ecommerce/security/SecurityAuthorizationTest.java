package com.eric.ecommerce.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.config.SecurityConfig;
import com.eric.ecommerce.controller.CategoriaController;
import com.eric.ecommerce.controller.ProductController;
import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.service.CategoriaService;
import com.eric.ecommerce.service.CustomUserDetailsService;
import com.eric.ecommerce.service.ProductService;

@WebMvcTest(controllers = { ProductController.class, CategoriaController.class })
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
}
