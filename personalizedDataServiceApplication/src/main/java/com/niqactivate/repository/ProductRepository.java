package com.niqactivate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import PersonalizedDataAPIEntity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.shopperId = :shopperId AND p.category = :category AND p.brand = :brand")
	List<Product> findByShopperIdAndCategoryAndBrand(String shopperId, String category, String brand, int limit);

	@Query("SELECT p FROM Product p WHERE p.shopperId = :shopperId AND p.category = :category")
	List<Product> findByShopperIdAndCategory(String shopperId, String category, int limit);

	@Query("SELECT p FROM Product p WHERE p.shopperId = :shopperId AND p.brand = :brand")
	List<Product> findByShopperIdAndBrand(String shopperId, String brand, int limit);

	List<Product> findByShopperId(String shopperId, int limit);



}
