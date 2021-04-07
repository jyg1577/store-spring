package com.example.store.product;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(columnDefinition = "CHAR(8)")
	private String code;
	private String category;
	private String name;
	private long price;
	private long quantity;
	private String shortDescription;
	private String description;

	private String managerId;
	private String businessNumber;

	@OneToMany
	@JoinColumn(name = "productId")
	private List<ProductImage> images;
}