package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record CreateProductCommand(
        String sku,
        String mnemonicCode,
        String name,
        String brandName,
        String category,
        String spec,
        String unit,
        String auxiliaryUnit,
        String auxiliarySpec,
        String packageUnit,
        String packageSpec,
        BigDecimal unitWeight,
        BigDecimal minOrderQty,
        BigDecimal minStock,
        BigDecimal maxStock,
        BigDecimal smallColorStock,
        Integer stockAgeThreshold,
        Integer slowMovingDays,
        Integer shelfLifeDays,
        String productAttribute,
        Boolean virtualProduct,
        Boolean installationIncluded,
        Boolean cloudStoreVisible,
        String productionProcess,
        String productionOrigin,
        String usageLocation,
        String vrUrl,
        String remark,
        BigDecimal purchasePrice,
        BigDecimal salePrice
) {
    public CreateProductCommand {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (brandName == null || brandName.isBlank()) {
            throw new IllegalArgumentException("brandName required");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("unit required");
        }
        if (productAttribute == null || productAttribute.isBlank()) {
            throw new IllegalArgumentException("productAttribute required");
        }
    }
}
