package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.PurchaseOrder;

import java.util.Optional;

public interface PurchaseOrderRepository {

    Optional<PurchaseOrder> findById(Long id);

    Optional<PurchaseOrder> findByOrderNo(String orderNo);

    PageResult<PurchaseOrder> page(long page, long size);

    PurchaseOrder save(PurchaseOrder order);

    PurchaseOrder updateStatus(Long id, String status);
}
