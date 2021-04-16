package com.example.store.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findByProductName(String productName);

	List<Cart> findByPurchaseState(String purchaseState);

}
