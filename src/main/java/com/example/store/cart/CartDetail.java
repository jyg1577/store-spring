package com.example.store.cart;

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
public class CartDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String productname;
	private long price;
	private long quantity;
	private long purchaseOrderId;
	private String imageName;
	private String contentType;

	public String getDataUrl() {
		return "http://localhost:8080" + "/product-images" + this.id;
	}
}