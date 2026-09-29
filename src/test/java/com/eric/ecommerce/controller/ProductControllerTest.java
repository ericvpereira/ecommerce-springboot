package com.eric.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.dto.ProductDTO;
import com.eric.ecommerce.exceptions.CategoriaNotFoundException;
import com.eric.ecommerce.exceptions.InvalidPriceRangeException;
import com.eric.ecommerce.exceptions.ProductNotFoundException;
import com.eric.ecommerce.service.ProductService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

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

	@Test
	void deveRetornarBadRequestQuandoFaixaDePrecoForInvalida() throws Exception {

		when(productService.findWithFilters(isNull(), isNull(), eq(new BigDecimal("5000")), eq(new BigDecimal("1000")),
				any(Pageable.class)))
				.thenThrow(new InvalidPriceRangeException("Preço mínimo não pode ser maior que preço máximo"));

		mockMvc.perform(get("/api/products").param("precoMin", "5000").param("precoMax", "1000"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Faixa de preço inválida"))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.errors.preco").value("Preço mínimo não pode ser maior que preço máximo"));

		verify(productService).findWithFilters(isNull(), isNull(), eq(new BigDecimal("5000")),
				eq(new BigDecimal("1000")), any(Pageable.class));

	}

	@Test
	void deveRetornarNotFoundQuandoCategoriaNaoExiste() throws Exception {

		when(productService.findWithFilters(eq(999), isNull(), isNull(), isNull(), any(Pageable.class)))
				.thenThrow(new CategoriaNotFoundException("Categoria com ID [999] não encontrada"));

		mockMvc.perform(get("/api/products").param("categoriaId", "999")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Categoria não encontrada"));

		verify(productService).findWithFilters(eq(999), isNull(), isNull(), isNull(), any(Pageable.class));

	}

	@Test
	void deveCriarProdutoComSucesso() throws Exception {

		ProductDTO dtoEntrada = new ProductDTO(null, "Notebook Gamer", new BigDecimal("4500"), 1);
		ProductDTO dtoSaida = new ProductDTO(1, "Notebook Gamer", new BigDecimal("4500"), 1);

		when(productService.save(any(ProductDTO.class))).thenReturn(dtoSaida);

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dtoEntrada))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.nome").value("Notebook Gamer"))
				.andExpect(jsonPath("$.preco").value(4500)).andExpect(jsonPath("$.categoriaId").value(1));

		ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);

		verify(productService).save(captor.capture());

		ProductDTO capturado = captor.getValue();

		assertNull(capturado.getId());

		assertEquals("Notebook Gamer", capturado.getNome());

		assertEquals(new BigDecimal("4500"), capturado.getPreco());

		assertEquals(1, capturado.getCategoriaId());
	}

	@Test
	void deveRetornarBadRequestQuandoProdutoForInvalido() throws Exception {

		ProductDTO dto = new ProductDTO(null, "", new BigDecimal("-100"), null);

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Erro de validação"))
				.andExpect(jsonPath("$.errors.nome").exists()).andExpect(jsonPath("$.errors.preco").exists())
				.andExpect(jsonPath("$.errors.categoriaId").exists());
		verifyNoInteractions(productService);

	}

	@Test
	void deveAtualizarProdutoComSucesso() throws Exception {

		ProductDTO dtoEntrada = new ProductDTO(null, "Notebook Gamer Pro", new BigDecimal("5500"), 1);
		ProductDTO dtoSaida = new ProductDTO(1, "Notebook Gamer Pro", new BigDecimal("5500"), 1);

		when(productService.update(eq(1), any(ProductDTO.class))).thenReturn(dtoSaida);

		mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dtoEntrada))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.nome").value("Notebook Gamer Pro"))
				.andExpect(jsonPath("$.preco").value(5500)).andExpect(jsonPath("$.categoriaId").value(1));

		ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);

		verify(productService).update(eq(1), captor.capture());

		ProductDTO capturado = captor.getValue();

		assertNull(capturado.getId());

		assertEquals("Notebook Gamer Pro", capturado.getNome());

		assertEquals(new BigDecimal("5500"), capturado.getPreco());

		assertEquals(1, capturado.getCategoriaId());
	}

	@Test
	void deveRetornarBadRequestQuandoAtualizacaoForInvalida() throws Exception {

		ProductDTO dto = new ProductDTO(null, "", new BigDecimal("-100"), null);

		mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Erro de validação"))
				.andExpect(jsonPath("$.errors.nome").exists()).andExpect(jsonPath("$.errors.preco").exists())
				.andExpect(jsonPath("$.errors.categoriaId").exists()).andExpect(status().isBadRequest());

		verifyNoInteractions(productService);

	}

	@Test
	void deveExcluirProdutoComSucesso() throws Exception {

		mockMvc.perform(delete("/api/products/1")).andExpect(status().isNoContent());

		verify(productService).deleteById(1);

	}

	@Test
	void deveRetornarNotFoundAoExcluirProdutoInexistente() throws Exception {

		doThrow(new ProductNotFoundException("Produto com ID [999] não encontrado")).when(productService)
				.deleteById(999);

		mockMvc.perform(delete("/api/products/999")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").exists());

		verify(productService).deleteById(999);

	}

	@Test
	void deveBuscarProdutoPorIdComSucesso() throws Exception {

		ProductDTO dto = new ProductDTO(1, "Notebook Gamer", new BigDecimal("4500"), 1);

		when(productService.findById(1)).thenReturn(dto);

		mockMvc.perform(get("/api/products/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Notebook Gamer")).andExpect(jsonPath("$.preco").value(4500))
				.andExpect(jsonPath("$.categoriaId").value(1));

		verify(productService).findById(1);

	}

	@Test
	void deveRetornarNotFoundQuandoProdutoNaoExiste() throws Exception {

		when(productService.findById(999))
				.thenThrow(new ProductNotFoundException("Produto com ID [999] não encontrado"));

		mockMvc.perform(get("/api/products/999")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").exists());

		verify(productService).findById(999);

	}

}
