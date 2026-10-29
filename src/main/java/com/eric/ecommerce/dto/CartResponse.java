package com.eric.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class CartResponse {

	@Schema(description = "Identificador do carrinho", example = "1")
	private Integer id;

	@Schema(description = "Lista de itens do carrinho")
	private List<CartItemResponse> items;

	@Schema(description = "Valor total do carrinho", example = "10500.00")
	private BigDecimal total;

	public CartResponse() {
	}

	public CartResponse(Integer id, List<CartItemResponse> items, BigDecimal total) {
		this.id = id;
		this.items = items;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CartItemResponse> getItems() {
		return items;
	}

	public void setItems(List<CartItemResponse> items) {
		this.items = items;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
