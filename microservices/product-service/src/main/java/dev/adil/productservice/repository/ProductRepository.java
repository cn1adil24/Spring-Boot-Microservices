package dev.adil.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.adil.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
