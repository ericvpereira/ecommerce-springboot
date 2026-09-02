package com.eric.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Categoria {
	
	@Id
	private Integer id;
	
	private String nome;

	public Categoria(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

}
