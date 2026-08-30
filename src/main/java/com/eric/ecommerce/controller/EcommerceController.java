package com.eric.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EcommerceController {

	@GetMapping("/api")
	public String metodo() {
		return "E-commerce API funcionando!";
	}

}
