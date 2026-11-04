package com.cailu.bom.erp.interfaces;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String sku,
        String mnemonicCode,
        @NotBlank String name,
        @NotBlank String brandName,
        String category,
        String spec,
        @NotBlank String unit,
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
        @NotBlank String productAttribute,
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
}
