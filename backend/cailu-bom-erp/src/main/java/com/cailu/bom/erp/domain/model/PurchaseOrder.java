package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PurchaseOrder {

    private final Long id;
    private final String orderNo;
    private final Long supplierId;
    private final Long warehouseId;
    private final String status;
    private final BigDecimal totalAmount;
    private final String remark;
    private final List<PurchaseOrderItem> items;

    public PurchaseOrder(Long id, String orderNo, Long supplierId, Long warehouseId,
                         String status, BigDecimal totalAmount, String remark,
                         List<PurchaseOrderItem> items) {
        if (orderNo == null || orderNo.isBlank()) {
            throw new IllegalArgumentException("orderNo required");
        }
        if (supplierId == null) {
            throw new IllegalArgumentException("supplierId required");
        }
        if (warehouseId == null) {
            throw new IllegalArgumentException("warehouseId required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items required");
        }
        this.id = id;
        this.orderNo = orderNo;
        this.supplierId = supplierId;
        this.warehouseId = warehouseId;
        this.status = status == null ? "draft" : status;
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
        this.remark = remark;
        this.items = List.copyOf(items);
    }

    public PurchaseOrder markCompleted() {
        if (!"draft".equals(status)) {
            throw new IllegalArgumentException("only draft purchase orders can be inbound");
        }
        return new PurchaseOrder(id, orderNo, supplierId, warehouseId, "completed",
                totalAmount, remark, items);
    }
}
