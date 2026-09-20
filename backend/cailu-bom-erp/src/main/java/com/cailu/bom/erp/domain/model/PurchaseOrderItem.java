package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PurchaseOrderItem {

    private final Long id;
    private final Long productId;
    private final BigDecimal qty;
    private final BigDecimal price;
    private final BigDecimal amount;

    public PurchaseOrderItem(Long id, Long productId, BigDecimal qty, BigDecimal price, BigDecimal amount) {
        if (productId == null) {
            throw new IllegalArgumentException("productId required");
        }
        if (qty == null || qty.signum() <= 0) {
            throw new IllegalArgumentException("qty must be positive");
        }
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
        this.id = id;
        this.productId = productId;
        this.qty = qty;
        this.price = price;
        this.amount = amount == null ? qty.multiply(price) : amount;
    }
}
