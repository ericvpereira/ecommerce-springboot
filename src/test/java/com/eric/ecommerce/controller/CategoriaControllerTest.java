package com.eric.ecommerce.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.service.CategoriaService;

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoriaService categoriaService;

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

}
