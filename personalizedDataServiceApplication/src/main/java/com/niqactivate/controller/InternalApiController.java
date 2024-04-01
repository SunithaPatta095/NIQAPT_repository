package com.niqactivate.controller;

import com.niqactivate.entity.Product;
import com.niqactivate.entity.Shopper;
import com.niqactivate.exception.ResourceNotFoundException;
import com.niqactivate.service.ProductService;
import com.niqactivate.service.ShopperService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/internal")
public class InternalApiController {

	private final ShopperService shopperService;
	private final ProductService productService;
	private final EntityManager entityManager;

	@Autowired
	public InternalApiController(ShopperService shopperService, ProductService productService,
			EntityManager entityManager) {
		this.shopperService = shopperService;
		this.productService = productService;
		this.entityManager = entityManager;
	}

	@PostMapping("/shopper")
	public ResponseEntity<?> storeShopperPersonalizedProductList(@RequestBody Shopper shopper) {
		shopperService.saveShopperPersonalizedProductList(shopper);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/product")
	public ResponseEntity<?> storeProductMetadata(@RequestBody PersonalizedDataAPIEntity.Product product) {
		productService.saveProductMetadata(product);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProductsByShopper(@RequestParam String shopperId,
			@RequestParam(required = false) String category, @RequestParam(required = false) String brand,
			@RequestParam(defaultValue = "10") int limit) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> cq = cb.createQuery(Product.class);
		Root<Product> root = cq.from(Product.class);
		cq.select(root);

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(root.get("shopperId"), shopperId));

		if (category != null) {
			predicates.add(cb.equal(root.get("category"), category));
		}
		if (brand != null) {
			predicates.add(cb.equal(root.get("brand"), brand));
		}

		cq.where(predicates.toArray(new Predicate[0]));

		List<Product> products = entityManager.createQuery(cq).setMaxResults(limit).getResultList();
		if (products.isEmpty()) {
			throw new ResourceNotFoundException("No products found for the given criteria");
		}
		return ResponseEntity.ok(products);
	}
}
