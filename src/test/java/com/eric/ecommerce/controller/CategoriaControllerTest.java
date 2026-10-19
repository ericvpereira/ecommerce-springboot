package com.eric.ecommerce.controller;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import com.eric.ecommerce.security.JwtAuthenticationFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.exceptions.CategoriaInUseException;
import com.eric.ecommerce.service.CategoriaService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CategoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoriaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoriaService categoriaService;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Test
	void deveListarCategoriasComSucesso() throws Exception {

		CategoriaDTO informatica = new CategoriaDTO(1, "Informatica");

		CategoriaDTO esportes = new CategoriaDTO(2, "Esportes");

		when(categoriaService.findAll()).thenReturn(List.of(informatica, esportes));

		mockMvc.perform(get("/api/categories")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].nome").value("Informatica")).andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].nome").value("Esportes"));

		verify(categoriaService).findAll();

	}

	@Test
	void deveBuscarCategoriaPorIdComSucesso() throws Exception {

		CategoriaDTO categoria = new CategoriaDTO(1, "Informatica");

		when(categoriaService.findDTOById(1)).thenReturn(categoria);

		mockMvc.perform(get("/api/categories/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Informatica"));

		verify(categoriaService).findDTOById(1);

	}

	@Test
	void deveCriarCategoriaComSucesso() throws Exception {

		CategoriaDTO entrada = new CategoriaDTO(null, "Informatica");

		CategoriaDTO saida = new CategoriaDTO(1, "Informatica");

		when(categoriaService.save(any(CategoriaDTO.class))).thenReturn(saida);

		mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entrada))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.nome").value("Informatica"));

		verify(categoriaService).save(any(CategoriaDTO.class));

	}

	@Test
	void deveAtualizarCategoriaComSucesso() throws Exception {

		CategoriaDTO entrada = new CategoriaDTO(null, "Eletronicos");

		CategoriaDTO saida = new CategoriaDTO(1, "Eletronicos");

		when(categoriaService.update(eq(1), any(CategoriaDTO.class))).thenReturn(saida);

		mockMvc.perform(put("/api/categories/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entrada))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.nome").value("Eletronicos"));

		verify(categoriaService).update(eq(1), any(CategoriaDTO.class));

	}

	@Test
	void deveExcluirCategoriaComSucesso() throws Exception {

		mockMvc.perform(delete("/api/categories/1")).andExpect(status().isNoContent());

		verify(categoriaService).deleteById(1);

	}

	@Test
	void deveRetornarConflitoAoExcluirCategoriaEmUso() throws Exception {

		doThrow(new CategoriaInUseException("A categoria não pode ser excluída porque possui produtos associados"))
				.when(categoriaService).deleteById(1);

		mockMvc.perform(delete("/api/categories/1")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409)).andExpect(jsonPath("$.message").value("Categoria em uso"))
				.andExpect(jsonPath("$.errors.categoria")
						.value("A categoria não pode ser excluída porque possui produtos associados"));

		verify(categoriaService).deleteById(1);

	}

}