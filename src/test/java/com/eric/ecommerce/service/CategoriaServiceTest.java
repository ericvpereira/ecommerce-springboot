package com.eric.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.exceptions.CategoriaNotFoundException;
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

	@Test
	void deveLancarExcecaoQuandoCategoriaNaoExiste() {

		when(categoriaRepository.findById(999)).thenReturn(Optional.empty());

		assertThrows(CategoriaNotFoundException.class, () -> categoriaService.findById(999));

		verify(categoriaRepository).findById(999);

	}

	@Test
	void deveCriarCategoriaComSucesso() {

		CategoriaDTO dto = new CategoriaDTO(null, "Informatica");

		Categoria categoriaSalva = new Categoria();
		categoriaSalva.setId(1);
		categoriaSalva.setNome("Informatica");

		when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaSalva);

		CategoriaDTO resultado = categoriaService.save(dto);

		assertEquals(1, resultado.getId());
		assertEquals("Informatica", resultado.getNome());

		verify(categoriaRepository).save(any(Categoria.class));

	}

	@Test
	void deveAtualizarCategoriaComSucesso() {

		Categoria categoriaExistente = new Categoria();
		categoriaExistente.setId(1);
		categoriaExistente.setNome("Informatica");

		when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaExistente));

		CategoriaDTO dto = new CategoriaDTO(null, "Eletronicos");

		when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

		CategoriaDTO resultado = categoriaService.update(1, dto);

		assertEquals(1, resultado.getId());
		assertEquals("Eletronicos", resultado.getNome());

		verify(categoriaRepository).findById(1);
		verify(categoriaRepository).save(any(Categoria.class));

	}

	@Test
	void deveExcluirCategoriaComSucesso() {

		Categoria categoriaExistente = new Categoria();
		categoriaExistente.setId(1);
		categoriaExistente.setNome("Informatica");

		when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaExistente));

		categoriaService.deleteById(1);

		verify(categoriaRepository).findById(1);
		verify(categoriaRepository).delete(categoriaExistente);

	}

}
