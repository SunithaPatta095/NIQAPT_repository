package com.niqactivate.service;

import com.niqactivate.entity.Shopper;
import com.niqactivate.exception.DuplicateEntryException;
import com.niqactivate.exception.ResourceNotFoundException;
import com.niqactivate.repository.ShopperRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShopperServiceImpl implements ShopperService {

	private final ShopperRepository shopperRepository;
	private final EntityManager entityManager;

	@Autowired
	public ShopperServiceImpl(ShopperRepository shopperRepository, EntityManager entityManager) {
		this.shopperRepository = shopperRepository;
		this.entityManager = entityManager;
	}

	@Override
	public Shopper createShopper(Shopper shopper) {
	    // Check if a shopper with the same email already exists
	    List<Shopper> existingShoppers = findByEmail(shopper.getShopperId());
	    if (!existingShoppers.isEmpty()) {
	        throw new DuplicateEntryException("Shopper with ShopperId '" + shopper.getShopperId() + "' already exists.");
	    }

	    // Save the new shopper entity
	    entityManager.persist(shopper);
	    return shopper;
	}

	@Override
	public List<PersonalizedDataAPIEntity.Shopper> getAllShoppers() {
		return shopperRepository.findAll();
	}

	@Override
	public PersonalizedDataAPIEntity.Shopper getShopperById(Long id) {
		return shopperRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shopper", "id", id));
	}

	@Override
	public Shopper saveShopper(Shopper shopper) {
		return shopperRepository.save(shopper);
	}

	@Override
	public Shopper saveShopperPersonalizedProductList(Shopper shopper) {
		PersonalizedDataAPIEntity.Shopper existingShopper = shopperRepository.findById(shopper.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Shopper"));

		existingShopper.setPersonalizedProductList(shopper.getshelf());

		return shopperRepository.save(existingShopper);
	}

	public List<Shopper> findByEmail(String email) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shopper> query = cb.createQuery(Shopper.class);
		Root<Shopper> root = query.from(Shopper.class);
		query.select(root).where(cb.equal(root.get("email"), email));
		return entityManager.createQuery(query).getResultList();
	}
}
