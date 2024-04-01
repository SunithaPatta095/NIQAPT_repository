package com.niqactivate.service;

import java.util.List;

import PersonalizedDataAPIEntity.Product;

public interface ProductService {

	Product createProduct(Product product);

	List<Product> getProductsByShopper(String shopperId, String category, String brand, int limit);

	Product saveProductMetadata(Product product);

}
