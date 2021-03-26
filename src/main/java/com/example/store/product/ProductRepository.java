package com.example.store.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	public List<Product> findByCode(String code);

	public List<Product> findByCategory(String category);

	public List<Product> findByName(String name);

	public List<Product> findByCreatedTimeLessThan(Long createdTime);
}