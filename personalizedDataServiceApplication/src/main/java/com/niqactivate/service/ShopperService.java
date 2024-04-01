package com.niqactivate.service;

import com.niqactivate.entity.Shopper;

import java.util.List;

public interface ShopperService {

	Shopper createShopper(Shopper shopper);

	List<PersonalizedDataAPIEntity.Shopper> getAllShoppers();

	PersonalizedDataAPIEntity.Shopper getShopperById(Long id);

	Shopper saveShopper(Shopper shopper);

	Shopper saveShopperPersonalizedProductList(Shopper shopper);
}
