package com.eric.ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@Test
	void deveBuscarProdutosComSucesso() throws Exception {

		ProductDTO dto = new ProductDTO(1, "Notebook Gamer", new BigDecimal("4500"), 1);

		Page<ProductDTO> page = new PageImpl<>(List.of(dto));

		when(productService.findWithFilters(isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
				.thenReturn(page);

		mockMvc.perform(get("/api/products")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].nome").value("Notebook Gamer"))
				.andExpect(jsonPath("$.content[0].preco").value(4500))
				.andExpect(jsonPath("$.content[0].categoriaId").value(1));

	}

	@Test
	void deveBuscarProdutosComFiltros() throws Exception {

		ProductDTO dto = new ProductDTO(1, "Notebook Gamer", new BigDecimal("4500"), 1);

		Page<ProductDTO> page = new PageImpl<>(List.of(dto));

		when(productService.findWithFilters(eq(1), eq("note"), eq(new BigDecimal("1000")), eq(new BigDecimal("5000")),
				any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/api/products").param("categoriaId", "1").param("nome", "note").param("precoMin", "1000")
				.param("precoMax", "5000")).andDo(print())

				.andExpect(status().isOk());

		verify(productService).findWithFilters(eq(1), eq("note"), eq(new BigDecimal("1000")),
				eq(new BigDecimal("5000")), any(Pageable.class));

	}

}
