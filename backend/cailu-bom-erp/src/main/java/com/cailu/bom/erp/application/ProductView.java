package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record ProductView(
        Long id,
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
        BigDecimal salePrice,
        Integer status
) {
}
