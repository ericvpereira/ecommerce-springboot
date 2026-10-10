package com.eric.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public class CategoriaDTO {

	@Schema(description = "Identificador único da categoria", example = "1")
	private Integer id;

	@NotBlank
	@Schema(description = "Nome da categoria", example = "Informática")
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
