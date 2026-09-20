package com.cailu.bom.erp.application;

import java.math.BigDecimal;
import java.util.List;

public record CreatePurchaseCommand(
        String orderNo,
        Long supplierId,
        Long warehouseId,
        String remark,
        List<Item> items
) {
    public CreatePurchaseCommand {
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
    }

    public record Item(Long productId, BigDecimal qty, BigDecimal price) {
        public Item {
            if (productId == null) {
                throw new IllegalArgumentException("productId required");
            }
            if (qty == null || qty.signum() <= 0) {
                throw new IllegalArgumentException("qty must be positive");
            }
            if (price == null || price.signum() < 0) {
                throw new IllegalArgumentException("price must be non-negative");
            }
        }
    }
}
