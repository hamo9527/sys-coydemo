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
        String packagingUnit,
        String packagingSpec,
        BigDecimal unitWeight,
        BigDecimal minimumOrderQty,
        BigDecimal minStock,
        BigDecimal maxStock,
        Boolean smallColorStock,
        Integer stockAgeThreshold,
        Integer obsoleteDays,
        Integer shelfLifeDays,
        String productAttribute,
        Boolean virtualProduct,
        Boolean installationRequired,
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
            throw new IllegalArgumentException("brand name required");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("unit required");
        }
        if (productAttribute == null || productAttribute.isBlank()) {
            throw new IllegalArgumentException("product attribute required");
        }
    }
}
