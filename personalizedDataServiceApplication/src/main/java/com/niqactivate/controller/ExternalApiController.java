package com.niqactivate.controller;

import com.niqactivate.entity.Product;
import com.niqactivate.repository.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/external/api")
public class ExternalApiController {

	private final EntityManager entityManager;

	@Autowired
	public ExternalApiController(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProductsByShopper(@RequestParam String shopperId,
			@RequestParam(required = false) String category, @RequestParam(required = false) String brand,
			@RequestParam(defaultValue = "10") int limit) {

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
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
}
