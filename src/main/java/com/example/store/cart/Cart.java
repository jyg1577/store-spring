package com.example.store.cart;

import java.util.List;

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
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String userId;
	private String userName;
	private String userAddress;

	// 구매누르면 ture, 장바구니에만 있을시 false
	private boolean purchaseState;

	private String category;
	private String productName;
	private long quantity;
	private long price;

	private long createdTime;

	@OneToMany
	@JoinColumn(name = "purchaseOrderId")
	private List<CartDetail> details;

}