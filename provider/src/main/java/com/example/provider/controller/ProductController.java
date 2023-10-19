package com.example.provider.controller;

import com.example.model.Product;
import com.example.provider.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
	private final ProductRepository productRepository;

	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping("products")
	public List<Product> getAllProducts() {
		return productRepository.fetchAll();
	}

	@GetMapping("product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") String id) {
		Optional<Product> product = productRepository.getById(id);

		return ResponseEntity.of(product);
	}
}
