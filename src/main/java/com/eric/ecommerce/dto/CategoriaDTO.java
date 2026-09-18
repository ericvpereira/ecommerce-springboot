package com.eric.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaDTO {

	private Integer id;

	@NotBlank
	private String nome;

	public CategoriaDTO() {

	}

	public CategoriaDTO(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
