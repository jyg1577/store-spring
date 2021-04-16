package com.example.store.product;

import javax.persistence.Entity;
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
	private long id;
	private String imageName;
	private String contentType;
	private long productId;

	private String dataUrl;
}
