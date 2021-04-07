
package com.example.store.order;

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
public class PurchaseOrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String category;
	private String code;
	private long purchaseOrderId;
	private long price;
	private long quantity;

}