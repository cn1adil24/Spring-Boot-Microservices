package dev.adil.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.adil.productservice.ddo.ProductRequest;
import dev.adil.productservice.ddo.ProductResponse;
import dev.adil.productservice.model.Product;
import dev.adil.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	public void addProduct(ProductRequest productDDO) {
		Product product = Product.builder()
				.name(productDDO.getName())
				.description(productDDO.getDescription())
				.price(productDDO.getPrice())
				.build();
		
		productRepository.save(product);
		
		// from Slf4j
		log.info("Product {} is saved.", product.getId());
	}
	
	public List<ProductResponse> getAllProducts() {
		List<Product> productList = productRepository.findAll();
		
		return productList.stream()
				.map(this::mapToProductResponse)
				.toList();
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}
}
