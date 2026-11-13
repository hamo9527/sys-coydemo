package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Stock {

    private final Long id;
    private final Long warehouseId;
    private final Long productId;
    private final String colorCode;
    private final String batchNo;
    private final String grade;
    private final String binName;
    private final BigDecimal quantity;
    private final BigDecimal lockedQty;

    public Stock(Long id, Long warehouseId, Long productId, String colorCode, String batchNo,
                 String grade, String binName, BigDecimal quantity, BigDecimal lockedQty) {
        if (warehouseId == null) {
            throw new IllegalArgumentException("warehouseId required");
        }
        if (productId == null) {
            throw new IllegalArgumentException("productId required");
        }
        this.id = id;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.colorCode = normalize(colorCode);
        this.batchNo = normalize(batchNo);
        this.grade = normalize(grade);
        this.binName = normalize(binName);
        this.quantity = quantity == null ? BigDecimal.ZERO : quantity;
        this.lockedQty = lockedQty == null ? BigDecimal.ZERO : lockedQty;
    }

    public Stock increase(BigDecimal delta) {
        if (delta == null || delta.signum() <= 0) {
            throw new IllegalArgumentException("increase qty must be positive");
        }
        return new Stock(id, warehouseId, productId, colorCode, batchNo, grade, binName,
                quantity.add(delta), lockedQty);
    }

    public Stock decrease(BigDecimal delta) {
        if (delta == null || delta.signum() <= 0) {
            throw new IllegalArgumentException("decrease qty must be positive");
        }
        if (quantity.compareTo(delta) < 0) {
            throw new IllegalArgumentException("insufficient stock");
        }
        return new Stock(id, warehouseId, productId, colorCode, batchNo, grade, binName,
                quantity.subtract(delta), lockedQty);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
