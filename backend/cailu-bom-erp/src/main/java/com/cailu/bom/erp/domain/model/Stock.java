package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Stock {

    private final Long id;
    private final Long warehouseId;
    private final Long productId;
    private final BigDecimal quantity;
    private final BigDecimal lockedQty;

    public Stock(Long id, Long warehouseId, Long productId, BigDecimal quantity, BigDecimal lockedQty) {
        if (warehouseId == null) {
            throw new IllegalArgumentException("warehouseId required");
        }
        if (productId == null) {
            throw new IllegalArgumentException("productId required");
        }
        this.id = id;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity == null ? BigDecimal.ZERO : quantity;
        this.lockedQty = lockedQty == null ? BigDecimal.ZERO : lockedQty;
    }

    public Stock increase(BigDecimal delta) {
        if (delta == null || delta.signum() <= 0) {
            throw new IllegalArgumentException("increase qty must be positive");
        }
        return new Stock(id, warehouseId, productId, quantity.add(delta), lockedQty);
    }

    public Stock decrease(BigDecimal delta) {
        if (delta == null || delta.signum() <= 0) {
            throw new IllegalArgumentException("decrease qty must be positive");
        }
        if (quantity.compareTo(delta) < 0) {
            throw new IllegalArgumentException("insufficient stock");
        }
        return new Stock(id, warehouseId, productId, quantity.subtract(delta), lockedQty);
    }
}
