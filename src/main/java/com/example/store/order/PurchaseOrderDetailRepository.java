package com.example.store.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {

	List<PurchaseOrderDetail> findBypurchaseOrderId(long purchaseOrderId);

}
