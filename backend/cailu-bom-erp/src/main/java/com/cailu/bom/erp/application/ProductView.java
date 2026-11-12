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
        BigDecimal salePrice,
        Integer status
) {
}
