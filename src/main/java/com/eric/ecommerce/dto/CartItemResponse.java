package com.eric.ecommerce.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public class CartItemResponse {

	@Schema(description = "Identificador do produto", example = "1")
	private Integer productId;

	@Schema(description = "Nome do produto", example = "Notebook Gamer")
	private String nome;

	@Schema(description = "Preço do produto", example = "4500.00")
	private BigDecimal preco;

	@Schema(description = "Quantidade do produto", example = "2")
	private Integer quantidade;

	@Schema(description = "Subtotal do item no carrinho", example = "7000.00")
	private BigDecimal subtotal;

	public CartItemResponse() {
	}

	public CartItemResponse(Integer productId, String nome, BigDecimal preco, Integer quantidade, BigDecimal subtotal) {
		this.productId = productId;
		this.nome = nome;
		this.preco = preco;
		this.quantidade = quantidade;
		this.subtotal = subtotal;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

}
