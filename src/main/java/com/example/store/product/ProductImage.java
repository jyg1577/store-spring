package com.example.store.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String imageName;
	private String contentType;
	private long productId;

	public String getDataUrl() {
		return "http://localhost:8080" + "/product-images" + this.id;
	}
//	@Transient
//	private String dataUrl;
}