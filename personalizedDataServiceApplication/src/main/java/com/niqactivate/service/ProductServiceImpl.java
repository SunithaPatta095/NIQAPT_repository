package com.niqactivate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niqactivate.repository.ProductRepository;

import PersonalizedDataAPIEntity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final EntityManager entityManager;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, EntityManager entityManager) {
		this.productRepository = productRepository;
		this.entityManager = entityManager;
	}

	@Override
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product saveProductMetadata(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getProductsByShopper(String shopperId, String category, String brand, int limit) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> cq = cb.createQuery(Product.class);
		Root<Product> root = cq.from(Product.class);
		cq.select(root).where(cb.equal(root.get("shopperId"), shopperId));

		if (category != null) {
			cq.where(cb.and(cb.equal(root.get("category"), category)));
		}
		if (brand != null) {
			cq.where(cb.and(cb.equal(root.get("brand"), brand)));
		}

		List<Product> products = entityManager.createQuery(cq).setMaxResults(limit).getResultList();
		return products;
	}

	public List<Product> findByShopperIdAndFilters(String shopperId, String category, String brand, int limit) {
		List<Product> products;
		if (category != null && brand != null) {
			products = productRepository.findByShopperIdAndCategoryAndBrand(shopperId, category, brand, limit);
		} else if (category != null) {
			products = productRepository.findByShopperIdAndCategory(shopperId, category, limit);
		} else if (brand != null) {
			products = productRepository.findByShopperIdAndBrand(shopperId, brand, limit);
		} else {
			products = productRepository.findByShopperId(shopperId, limit);
		}
		// limit
		if (limit > 0 && limit < products.size()) {
			return products.subList(0, limit);
		} else {
			return products;
		}
	}

}
