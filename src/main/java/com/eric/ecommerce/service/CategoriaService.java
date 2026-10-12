package com.eric.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import com.eric.ecommerce.repository.ProductRepository;
import com.eric.ecommerce.exceptions.CategoriaInUseException;

import org.springframework.stereotype.Service;

import com.eric.ecommerce.dto.CategoriaDTO;
import com.eric.ecommerce.exceptions.CategoriaNotFoundException;
import com.eric.ecommerce.model.Categoria;
import com.eric.ecommerce.repository.CategoriaRepository;

@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;

	private final ProductRepository productRepository;

	public CategoriaService(CategoriaRepository categoriaRepository, ProductRepository productRepository) {
		this.categoriaRepository = categoriaRepository;
		this.productRepository = productRepository;
	}

	public CategoriaDTO save(CategoriaDTO dto) {

		Categoria categoria = toEntity(dto);

		Categoria savedCategoria = categoriaRepository.save(categoria);

		return toDTO(savedCategoria);

	}

	public List<CategoriaDTO> findAll() {

		List<Categoria> categorias = categoriaRepository.findAll();

		List<CategoriaDTO> dtos = new ArrayList<>();

		for (Categoria categoria : categorias) {
			dtos.add(toDTO(categoria));
		}

		return dtos;

	}

	public Categoria findById(Integer id) {

		return categoriaRepository.findById(id)
				.orElseThrow(() -> new CategoriaNotFoundException("Categoria com ID [" + id + "] não encontrada"));

	}

	public CategoriaDTO update(Integer id, CategoriaDTO dto) {

		Categoria categoria = findById(id);
		categoria.setNome(dto.getNome());
		Categoria savedCategoria = categoriaRepository.save(categoria);

		return toDTO(savedCategoria);

	}

	public void deleteById(Integer id) {

		Categoria categoria = findById(id);

		if (productRepository.existsByCategoria_Id(id)) {

			throw new CategoriaInUseException("A categoria não pode ser excluída porque possui produtos associados");

		}

		categoriaRepository.delete(categoria);

	}

	public CategoriaDTO findDTOById(Integer id) {

		Categoria categoria = findById(id);

		return toDTO(categoria);

	}

	public CategoriaDTO toDTO(Categoria categoria) {

		CategoriaDTO dto = new CategoriaDTO();

		dto.setId(categoria.getId());
		dto.setNome(categoria.getNome());

		return dto;

	}

	public Categoria toEntity(CategoriaDTO dto) {

		Categoria categoria = new Categoria();

		categoria.setNome(dto.getNome());

		return categoria;

	}

}
