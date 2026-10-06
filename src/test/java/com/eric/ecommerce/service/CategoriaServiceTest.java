package com.eric.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

	@Mock
	private CategoriaRepository categoriaRepository;

	@InjectMocks
	private CategoriaService categoriaService;

	@Test
	void deveBuscarCategoriaPorId() {

		Categoria categoria = new Categoria();
		categoria.setId(1);
		categoria.setNome("Informatica");

		when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

		Categoria resultado = categoriaService.findById(1);

		assertEquals(1, resultado.getId());
		assertEquals("Informatica", resultado.getNome());

		verify(categoriaRepository).findById(1);

	}

}
