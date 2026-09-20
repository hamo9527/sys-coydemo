package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Product {

    private final Long id;
    private final String sku;
    private final String name;
    private final String unit;
    private final BigDecimal purchasePrice;
    private final BigDecimal salePrice;
    private final Integer status;

    public Product(Long id, String sku, String name, String unit,
                   BigDecimal purchasePrice, BigDecimal salePrice, Integer status) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.unit = unit;
        this.purchasePrice = purchasePrice == null ? BigDecimal.ZERO : purchasePrice;
        this.salePrice = salePrice == null ? BigDecimal.ZERO : salePrice;
        this.status = status == null ? 1 : status;
    }
}
